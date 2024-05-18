import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    stages: [
        { duration: '15s', target: 0 },
        { duration: '10s', target: 30 },
        { duration: '15s', target: 10 },
        { duration: '5s', target: 50 },
        { duration: '10s', target: 20 },
        { duration: '15s', target: 40 },
        { duration: '10s', target: 0 },
        { duration: '10s', target: 30 },
        { duration: '5s', target: 10 },
        { duration: '15s', target: 50 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<1000'],
    },
};

export default function () {
    const res = http.get('http://localhost:30080/info/hostname');

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
    }

    sleep(1);
}
