export class Token {

  constructor(token) {
    this.token = token;
    this._values = JSON.parse(atob(token));
  }

  get role() {
    return this._values.role;
  }

  toString() {
    return this.token;
  }
}