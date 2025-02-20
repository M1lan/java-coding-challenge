
# Table of Contents

-   [Overview This project implements a serverless microservice named](#orgc18cf3c)
-   [User Guide](#org04bc913)
    -   [Deployment](#org74ef4d4)
    -   [Usage After deployment, access your microservice via the generated](#org58e044a)
-   [Developer Guide](#orgeb76c81)
    -   [Project Structure](#orgbef7f8f)
    -   [Setup & Local Testing](#org5ebcecc)
-   [Learning Progress](#orgedd86be)
-   [Resources](#orgb3c6e79)
    -   [Rust](#org4e18316)
    -   [WebAssembly (WASM)](#orga65cfab)
    -   [Cloudflare Workers](#org07a7b8a)
    -   [DevOps](#org77b6591)
-   [DevOps Topics](#org87261f8)
-   [Future Directions](#org337a84c)
-   [Conclusion](#orgb08c0a0)



<a id="orgc18cf3c"></a>

# Overview This project implements a serverless microservice named

****cf_worker_wasm_microservice**** that compiles to WebAssembly (WASM) and
runs on Cloudflare Workers. The service asynchronously fetches:

-   User data from:
    <http://jsonplaceholder.typicode.com/users/1>
-   Post data from:
    <http://jsonplaceholder.typicode.com/posts?userId=1>

It then merges these JSON responses into a single payload and returns
the combined result over HTTP. This project showcases modern Rust
development, WASM integration, and DevOps practices for efficient
serverless deployments.


<a id="org04bc913"></a>

# User Guide


<a id="org74ef4d4"></a>

## Deployment

-   ****Prerequisites:****
    -   Rust (1.86.0-beta.1), Cargo (1.86.0-beta.1)
    -   Wrangler (v3.109.2)
    -   WASM target: \`wasm32-unknown-unknown\`
-   ****Build Commands:****

        rustup target add wasm32-unknown-unknown
        cargo build --lib --release --target wasm32-unknown-unknown
-   ****Deploy Commands:****

        wrangler deploy


<a id="org58e044a"></a>

## Usage After deployment, access your microservice via the generated

URL. Sending an HTTP GET request to the URL returns a merged JSON
response with user and post data.


<a id="orgeb76c81"></a>

# Developer Guide


<a id="orgbef7f8f"></a>

## Project Structure

-   ****Cargo.toml:**** Dependency and build configurations.
-   ****src/lib.rs:**** Contains the event handler for HTTP requests, global
    fetch logic via JS interop, and JSON processing.
-   Uses external crates: worker, serde, serde<sub>json</sub>, futures,
    wasm-bindgen, wasm-bindgen-futures, serde-wasm-bindgen, and web-sys.


<a id="org5ebcecc"></a>

## Setup & Local Testing

-   Clone the repository.
-   Build locally with the WASM target:

    cargo build --lib --release --target wasm32-unknown-unknown

-   Test functionality via Wrangler's dry-run:

        wrangler deploy --dry-run --outdir=dist


<a id="orgedd86be"></a>

# Learning Progress

In this project folder I am solving the challenge a second time.  This
is not my first adventure in Rust, or in WASM.  The main motivation here
was that I feel strongly that Java is a little bit convoluted. And that
Spring Boot (unlike Quarkus), might not be the best choice for a
"serverless" environment.  But also: I am not the biggest fan of
"serverless", and I think that WASM is the much better choice.

As a Bonus, we also get for-free hosting on Cloudflare, so we can
actually have this microservice live and deployed somewhere!

That alone makes it more joyful and more satisfying.  Also, we are
completely avoiding Docker here because we can directly compile from
Rust to WASM, and with the cloudflare toolchain also instantly deploy.

Then the one aspect I enjoyed MOST in comparison to Java: this is just
two files!  \`Cargo.toml\` and \`./src/lib.rs\` so this is much more
pleasant to work with than the standard java folder and class names
structure and naming conventions.  And also the error messages from
rustc are a lot nicer than javac.

-   ****Rust & Async Programming:**** Leveraging Rust’s asynchronous
    features to concurrently fetch remote data.
-   ****WebAssembly (WASM):**** Compiling Rust code to WASM to achieve
    low-latency, on-demand execution.
-   ****Cloudflare Workers:**** Integrating Rust WASM modules into
    Cloudflare’s serverless platform.
-   ****DevOps TODO :**** Setting up CI/CD pipelines, monitoring
    deployments, and cost optimization strategies in a serverless
    environment.


<a id="orgb3c6e79"></a>

# Resources


<a id="org4e18316"></a>

## Rust

-   [The Rust Programming Language Book](<https://doc.rust-lang.org/book/>)
-   [Rust Async Programming](<https://rust-lang.github.io/async-book/>)


<a id="orga65cfab"></a>

## WebAssembly (WASM)

-   [Rust and WebAssembly](<https://rustwasm.github.io/>)
-   [MDN WebAssembly Documentation](<https://developer.mozilla.org/en-US/docs/WebAssembly>)


<a id="org07a7b8a"></a>

## Cloudflare Workers

-   [Cloudflare Workers Documentation](<https://developers.cloudflare.com/workers/>)
-   [Workers RS GitHub Repository](<https://github.com/cloudflare/workers-rs>)


<a id="org77b6591"></a>

## DevOps

-   [Continuous Deployment with Wrangler](<https://developers.cloudflare.com/workers/wrangler/>)
-   [Serverless DevOps Best Practices](<https://www.serverless.com/blog/serverless-devops>)


<a id="org87261f8"></a>

# DevOps Topics

-   ****CI/CD:**** Automate building and deployment using GitHub Actions
    or GitLab CI.
-   ****Monitoring:**** Utilize Cloudflare’s analytics and logging tools
    to monitor worker performance.
-   ****Security:**** Apply best practices for securing serverless
    functions and managing API keys.
-   ****Cost Optimization:**** Focus on reducing cold start latency and
    resource usage to minimize costs.


<a id="org337a84c"></a>

# Future Directions

-   Optimize WASM compilation further to reduce cold start times.
-   Extend the microservice with additional endpoints or real-time data
    processing.
-   Integrate advanced DevOps workflows (e.g., blue-green deployments,
    canary releases).
-   Build an interactive front-end for monitoring and managing
    deployments.


<a id="orgb08c0a0"></a>

# Conclusion

The ****cf_worker_wasm_microservice**** project exemplifies modern
serverless development using Rust, WebAssembly, and Cloudflare Workers.
