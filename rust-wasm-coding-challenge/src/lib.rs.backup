use worker::*;
use futures::join;
use wasm_bindgen::JsCast;
use wasm_bindgen_futures::JsFuture;
use js_sys::{Promise, Reflect};
use wasm_bindgen::JsValue;
use web_sys::Response as WebResponse;
use serde_wasm_bindgen;

async fn fetch_url(url: &str) -> worker::Result<serde_json::Value> {

    // get the global fetch function
    let global = js_sys::global();
    let fetch_fn = Reflect::get(&global, &JsValue::from_str("fetch"))
        .map_err(|_| worker::Error::from("failed to get fetch function"))?;
    let fetch_fn = fetch_fn
        .dyn_into::<js_sys::Function>()
        .map_err(|_| worker::Error::from("global.fetch is not a function"))?;

    // call fetch(url)
    let promise = fetch_fn
        .call1(&JsValue::NULL, &JsValue::from_str(url))
        .map_err(|_| worker::Error::from("failed to call fetch"))?;
    let resp_js = JsFuture::from(Promise::from(promise))
        .await
        .map_err(|_| worker::Error::from("fetch promise rejected"))?;

    // convert to web_sys::Response
    let web_resp: WebResponse = resp_js
        .dyn_into()
        .map_err(|_| worker::Error::from("failed to convert to web_sys::Response"))?;

    // call response.json() to get a Promise for the JSON data
    let json_promise = web_resp
        .json()
        .map_err(|_| worker::Error::from("failed to call response.json()"))?;
    let json_js = JsFuture::from(json_promise)
        .await
        .map_err(|_| worker::Error::from("json promise rejected"))?;
    // deserialize into serde_json::Value using serde_wasm_bindgen
    let json: serde_json::Value = serde_wasm_bindgen::from_value(json_js)
        .map_err(|_| worker::Error::from("failed to deserialize JSON"))?;
    Ok(json)
}

#[event(fetch)]
pub async fn main(_req: Request, _env: Env, _ctx: Context) -> worker::Result<Response> {
    let user_url = "http://jsonplaceholder.typicode.com/users/1";
    let posts_url = "http://jsonplaceholder.typicode.com/posts?userId=1";

    // fetch both endpoitns concurrently
    let user_future = fetch_url(user_url);
    let posts_future = fetch_url(posts_url);
    let (user_json_result, posts_json_result) = join!(user_future, posts_future);
    let user_json = user_json_result?;
    let posts_json = posts_json_result?;

    let merged = serde_json::json!({
        "user": user_json,
        "posts": posts_json
    });

    // build response headers
    let mut headers = Headers::new();
    headers.append("Content-Type", "application/json")?;

    Response::ok(merged.to_string())
        .map(|resp| resp.with_headers(headers))
}
