import http from 'k6/http';
import { sleep, check } from 'k6';

// Define the base URL of the server to be tested
const BASE_URL = 'http://127.0.0.1:8080'; // Adjust the URL if your server runs on a different host/port

// Options for the k6 test
export let options = {
  // These options will be overridden by the command line arguments
  vus: 50, // Number of virtual users
  duration: '24h', // Duration of the test
  thresholds: {
    http_req_failed: ['rate<0.01'], // Less than 1% of requests may fail
    http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
  },
};

export default function () {
  // Simulate a user fetching merged data
  let response = http.get(`${BASE_URL}/`);

  // Check if the response status is 200 (OK)
  check(response, {
    'is status 200': (r) => r.status === 200,
  });

  // Optionally, check if the response body contains expected data
  check(response, {
    'has user data': (r) => r.body.includes('"user"'),
    'has posts data': (r) => r.body.includes('"posts"'),
  });

  // Sleep for a random amount of time between 1 and 5 seconds to simulate user think time
  sleep(Math.random() * 4 + 1);
}
