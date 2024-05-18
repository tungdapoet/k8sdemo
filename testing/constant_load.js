import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '20s', target: 40 },
        { duration: '20s', target: 40 },
        { duration: '20s', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<1000'],
        'http_req_failed': ['rate<0.01'],
    },
};

export default function () {
    const res = http.get('http://localhost:30080/info/hostname');

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
    }

    sleep(1);
}
