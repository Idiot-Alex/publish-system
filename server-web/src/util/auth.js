import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getValueByKey(key) {
  return Cookies.get(key)
}

export function setValue(key, value) {
  return Cookies.set(key, value)
}

export function removeValueByKey(key) {
  return Cookies.remove(key)
}
