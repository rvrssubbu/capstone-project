import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '60s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<300'],
  },
};

export default function () {
  const res = http.get('http://localhost:8083/api/v1/ledger/entries?merchantId=merchant-1&page=0&size=20');
  check(res, { 'status 200': (r) => r.status === 200 });
}
