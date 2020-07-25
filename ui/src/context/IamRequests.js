import { Token } from "./Token"

function createForm(username, password) {
  return `username=${username}&secret=${password}`;
  }

export function requestSignin(username, password, success = () => {}, failure = () => {} ) {
  fetch("/iam/signin", {
    method: "POST",
    body: createForm(username, password),
    headers: new Headers({
      'Content-Type': 'application/x-www-form-urlencoded'
      })
    })
    .then(response => {
      if (response.status === 200) {
        return response.json();
      } else {
        failure();
      }
    }).then(data => {
      success(new Token(data.token));
    }).catch(e => {
      failure();
    })
}

function authHeader() {
  const auth = sessionStorage.getItem("token");
  return `Bearer ${auth}`;
}

export function requestSignout(success = () => {}) {
  fetch("/iam/signout", {
      method: "POST",
      headers: new Headers({
        'Accept': 'application/json',
        'Authorization': authHeader()
      })
    }).then(response => {
      success();
      sessionStorage.removeItem("token");
    })
    .catch(e => {
      success();
      sessionStorage.removeItem("token");
    });
}