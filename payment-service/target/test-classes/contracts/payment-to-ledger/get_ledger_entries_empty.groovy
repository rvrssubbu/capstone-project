import org.springframework.cloud.contract.spec.Contract

Contract.make {
  request {
    method 'GET'
    urlPath('/api/v1/ledger/entries') {
      queryParameters {
        parameter 'merchantId': 'merchant-123'
        parameter 'page': '0'
        parameter 'size': '20'
      }
    }
  }
  response {
    status OK()
    headers {
      contentType(applicationJson())
    }
    body([
      content: [],
      page: 0,
      size: 20,
      totalElements: 0
    ])
  }
}
