export default class User {
  constructor(username, email, password,confirmPassword,firstname,lastname,phoneNumber,token) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.firstname = firstname;
    this.lastname = lastname;
    this.phoneNumber = phoneNumber;
    this.token = token;
  }
}
