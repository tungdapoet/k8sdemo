import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 200 },
        { duration: '1m', target: 200 },
        { duration: '1m', target: 0 },
        { duration: '1m', target: 0 },
        { duration: '1m', target: 200 },
        { duration: '1m', target: 200 },
        { duration: '1m', target: 0 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'],
    },
};

export default function () {
    const res = http.get('http://k8sdemo-app-service.application.svc.cluster.local/info/hostname');

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
    }

    sleep(1);
}
