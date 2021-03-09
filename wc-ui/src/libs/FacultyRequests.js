function authHeader() {
  const auth = sessionStorage.getItem("token");
  return `Bearer ${auth}`;
}

export function getFaculties(success = () => {}, failure = () => {}) {
  fetch("/faculties", {
      method: "GET",
      headers: new Headers({
        'Accept': 'application/json',
        'Authorization': authHeader()
      })
    }).then(response => {
      if (response.status === 200) {
        return response.json();
      } else {
        failure();
      }
    }).then(data => {
      success(data);
    }).catch(error => {
      failure();
    })
}