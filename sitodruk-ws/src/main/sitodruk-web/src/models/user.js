export default class User {
  constructor(username, email, password,confirmPassword,firstname,lastname,phoneNumber,token,dtoVersion,confirmed,active,roles,language) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.firstname = firstname;
    this.lastname = lastname;
    this.phoneNumber = phoneNumber;
    this.token = token;
    this.dtoVersion = dtoVersion;
    this.confirmed = confirmed;
    this.active = active;
    this.roles = roles;
    this.language = language;
  }
}
