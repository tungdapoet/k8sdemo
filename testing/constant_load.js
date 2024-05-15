import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '1m', target: 100 },
        { duration: '5m', target: 100 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
    },
};

export default function () {
    const res = http.get('http://k8sdemo-app-service.application.svc.cluster.local/info/hostname');

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
    }

    sleep(1);
}
