let baseUrl = ''
switch(process.env.NODE_ENV) {
  case 'production': 
    baseUrl = 'http://127.0.0.1:8080/publish/'
    break
  default:
    baseUrl = 'http://127.0.0.1:8080/publish/'
    break
}
export default baseUrl
