<template>
  <div class="col-md-12">
    <div class="card card-container">
      <form name="form" @submit.prevent="handleEdit">
        <div v-if="!successful">
          <div class="form-group">
            <label for="firstname">{{ $t('firstname') }}*</label>
            <input
              v-model="user.firstname"
              v-validate="'required|min:3|max:64'"
              type="text"
              class="form-control"
              name="firstname"
            />
            <div
              v-if="submitted && errors.has('firstname')"
              class="alert-danger"
            >
              {{ errors.first('firstname') }}
            </div>
          </div>
          <div class="form-group">
            <label for="lastname">{{ $t('lastname') }}*</label>
            <input
              v-model="user.lastname"
              v-validate="'required|min:3|max:100'"
              type="text"
              class="form-control"
              name="lastname"
            />
            <div
              v-if="submitted && errors.has('lastname')"
              class="alert-danger"
            >
              {{ errors.first('lastname') }}
            </div>
          </div>
          <div class="form-group">
            <label for="username">{{ $t('username') }}</label>
            <input
              v-model="user.username"
              disabled
              type="text"
              class="form-control"
              name="username"
            />
          </div>
          <div class="form-group">
            <label for="email">{{ $t('email') }}</label>
            <input
              v-model="user.email"
              disabled
              type="email"
              class="form-control"
              name="email"
            />
            <div class="form-group">
              <label for="phoneNumber">{{ $t('phoneNumber') }}*</label>
              <input
                v-model="user.phoneNumber"
                v-validate="{ required: true, digits: 9 }"
                type="text"
                class="form-control"
                name="phoneNumber"
              />
              <div
                v-if="submitted && errors.has('phoneNumber')"
                class="alert-danger"
              >
                {{ errors.first('phoneNumber') }}
              </div>
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-block">
                {{ $t('edit') }}
              </button>
            </div>
          </div>
        </div>
      </form>
      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ $t(message.message) }}
      </div>
    </div>
  </div>
</template>

<script>
import User from '../models/user';
import UserService from '../services/user.service';

export default {
  name: 'EditUser',
  data() {
    return {
      user: new User('', '', '', '', '', '', '', '', '', '', ''),
      submitted: false,
      successful: false,
      message: '',
    };
  },
  mounted() {
    if (this.$store.selectedUser == undefined) {
      this.$router.push({
        path: '/users',
      });
    } else {
      UserService.findAccount(this.$store.selectedUser).then(
        (data) => {
          this.user = new User(
            data.data.username,
            data.data.email,
            data.data.password,
            data.data.confirmPassword,
            data.data.firstname,
            data.data.lastname,
            data.data.phoneNumber,
            data.data.token,
            data.data.dtoVersion,
            data.data.confirmed,
            data.data.active,
            data.data.roles
          );
          data.data;
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch('auth/logout');
           this.$alert(this.$t('session.timed.out'));
                this.$router.push('/login');
          }
        }
      );
    }
  },
  methods: {
    handleEdit() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          UserService.editSelectedAccount(this.user).then(
            (data) => {
              this.message = data.message;
              this.successful = true;
              this.$alert(this.$t('account.has.been.edited'));
              this.$router.push('/users');
            },
            (error) => {
              this.message = error.response && error.response.data;
              if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$alert(this.$t('session.timed.out'));
                this.$router.push('/login');
              }
              this.successful = false;
            }
          );
        }
      });
    },
  },
};
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}
</style>
