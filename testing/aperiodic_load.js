import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '2m', target: 20 },
        { duration: '3m', target: 20 },
        { duration: '1m', target: 40 },
        { duration: '3m', target: 40 },
        { duration: '2m', target: 0 },
        { duration: '2m', target: 0 },
        { duration: '1m', target: 60 },
        { duration: '4m', target: 60 },
        { duration: '2m', target: 20 },
        { duration: '3m', target: 20 },
        { duration: '2m', target: 0 },
    ],
    thresholds: {
        'http_req_duration': ['p(95)<1000'],
        'http_req_failed': ['rate<0.01'],
    },
};

export default function () {
    const res = http.get('http://localhost:30080/info/hostname');  // Thay thế bằng endpoint của bạn

    if (res.status !== 200) {
        console.error(`Request failed. Status: ${res.status}`);
    }
    sleep(1);  // Mỗi VU gửi yêu cầu mỗi giây
}
