function authHeader() {
  const auth = sessionStorage.getItem("token");
  return `Bearer ${auth}`;
}

export function getProfessors(success = () => {}, failure = () => {}) {
  fetch("/professors", {
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

export function addProfessor(professor, success = () => {}, failure = () => {}) {
  fetch("/professors", {
        method: "POST",
        body: JSON.stringify(professor),
        headers: new Headers({
          'Content-type': 'application/json',
          'Authorization': authHeader()
        })
      }).then(response => {
        if (response.status === 201) {
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

export function updateProfessor(professor, success = () => {}, failure = () => {}) {
  const url = `/professors/${professor.id}`;
  fetch(url, {
        method: "PUT",
        body: JSON.stringify(professor),
        headers: new Headers({
          'Content-type': 'application/json',
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

export function deleteProfessor(professor, success = () => {}, failure = () => {}) {
  const url = `/professors/${professor.id}`;
  fetch(url, {
        method: "DELETE",
        headers: new Headers({
          'Content-type': 'application/json',
          'Authorization': authHeader()
        })
      }).then(response => {
        if (response.status === 204) {
          success();
        } else {
          failure();
        }
      }).catch(error => {
        failure();
      })
}
