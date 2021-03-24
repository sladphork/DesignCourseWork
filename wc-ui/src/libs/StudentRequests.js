function authHeader() {
  const auth = sessionStorage.getItem("token");
  return `Bearer ${auth}`;
}

export function getStudents(success = () => {}, failure = () => {}) {
  fetch("/students", {
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

export function addStudent(student, success = () => {}, failure = () => {}) {
  fetch("/students", {
        method: "POST",
        body: JSON.stringify(student),
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

export function updateStudent(student, success = () => {}, failure = () => {}) {
  const url = `/students/${student.id}`;
  fetch(url, {
        method: "PUT",
        body: JSON.stringify(student),
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

export function deleteStudent(student, success = () => {}, failure = () => {}) {
  const url = `/students/${student.id}`;
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
