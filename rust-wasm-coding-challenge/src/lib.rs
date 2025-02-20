use futures::join;
use js_sys::{Promise, Reflect};
use wasm_bindgen::JsCast;
use wasm_bindgen::JsValue;
use wasm_bindgen_futures::JsFuture;
use web_sys::Response as WebResponse;
use worker::*;

async fn fetch_url(url: &str) -> worker::Result<serde_json::Value> {
    let global = js_sys::global();
    let fetch_fn = Reflect::get(&global, &JsValue::from_str("fetch"))
        .map_err(|_| worker::Error::from("failed to get fetch function"))?;
    let fetch_fn = fetch_fn
        .dyn_into::<js_sys::Function>()
        .map_err(|_| worker::Error::from("global.fetch is not a function"))?;
    let promise = fetch_fn
        .call1(&JsValue::NULL, &JsValue::from_str(url))
        .map_err(|_| worker::Error::from("failed to call fetch"))?;
    let resp_js = JsFuture::from(Promise::from(promise))
        .await
        .map_err(|_| worker::Error::from("fetch promise rejected"))?;
    let web_resp: WebResponse = resp_js
        .dyn_into()
        .map_err(|_| worker::Error::from("failed to convert to web_sys::Response"))?;
    let json_promise = web_resp
        .json()
        .map_err(|_| worker::Error::from("failed to call response.json()"))?;
    let json_js = JsFuture::from(json_promise)
        .await
        .map_err(|_| worker::Error::from("json promise rejected"))?;
    let json: serde_json::Value = serde_wasm_bindgen::from_value(json_js)
        .map_err(|_| worker::Error::from("failed to deserialize JSON"))?;
    Ok(json)
}

async fn get_cached_response(_req: &Request) -> worker::Result<Option<Response>> {
    Ok(None)
}

async fn put_response_in_cache(_req: &Request, _resp: &Response) -> worker::Result<()> {
    Ok(())
}

#[event(fetch)]
pub async fn main(req: Request, _env: Env, _ctx: Context) -> worker::Result<Response> {
    if let Some(resp) = get_cached_response(&req).await? {
        return Ok(resp);
    }
    let user_url = "http://jsonplaceholder.typicode.com/users/1";
    let posts_url = "http://jsonplaceholder.typicode.com/posts?userId=1";
    let user_future = fetch_url(user_url);
    let posts_future = fetch_url(posts_url);
    let (user_json_result, posts_json_result) = join!(user_future, posts_future);
    let user_json = user_json_result?;
    let posts_json = posts_json_result?;
    let merged = serde_json::json!({
        "user": user_json,
        "posts": posts_json
    });
    let mut headers = Headers::new();
    headers.append("Content-Type", "application/json")?;
    let response = Response::ok(merged.to_string())?.with_headers(headers);
    put_response_in_cache(&req, &response).await?;
    Ok(response)
}
