<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img
        id="profile-img"
        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
        class="profile-img-card"
      />
      <form name="form" @submit.prevent="handleCreate">
        <div v-if="!successful">
          <div class="form-group">
            <label for="firstname">{{ $t('firstname') }}</label>
            <input
              v-model="user.firstname"
              v-validate="'required|min:3|max:20'"
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
            <label for="lastname">{{ $t('lastname') }}</label>
            <input
              v-model="user.lastname"
              v-validate="'required|min:2|max:20'"
              type="text"
              class="form-control"
              name="lastname"
            />
            <div
              v-if="submitted && errors.has('lastName')"
              class="alert-danger"
            >
              {{ errors.first('lastame') }}
            </div>
          </div>
          <div class="form-group">
            <label for="username">{{ $t('username') }}</label>
            <input
              v-model="user.username"
              v-validate="'required|min:3|max:20'"
              type="text"
              class="form-control"
              name="username"
            />
            <div
              v-if="submitted && errors.has('username')"
              class="alert-danger"
            >
              {{ errors.first('username') }}
            </div>
          </div>
          <div class="form-group">
            <label for="email">{{ $t('email') }}</label>
            <input
              v-model="user.email"
              v-validate="'required|email|max:50'"
              type="email"
              class="form-control"
              name="email"
            />
            <div v-if="submitted && errors.has('email')" class="alert-danger">
              {{ errors.first('email') }}
            </div>
          </div>
          <div class="form-group">
            <label for="password">{{ $t('password') }}</label>
            <input
              v-model="user.password"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="password"
            />
            <div
              v-if="submitted && errors.has('password')"
              class="alert-danger"
            >
              {{ errors.first('password') }}
            </div>
          </div>
          <div class="form-group">
            <label for="confirmPassword">{{ $t('confirmPassword') }}</label>
            <input
              v-model="user.confirmPassword"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="confirmPassword"
            />
            <div
              v-if="submitted && errors.has('confirmPassword')"
              class="alert-danger"
            >
              {{ errors.first('confirmPassword') }}
            </div>
          </div>
          <div class="form-group">
            <label for="phoneNumber">{{ $t('phoneNumber') }}</label>
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
            <label for="roles">{{ $t('select.user.roles') }}</label>
            <b-form-group id="roles" v-slot="{ ariaDescribedby }">
              <b-form-checkbox
                v-for="option in options"
                v-model="selectedRoles"
                :key="option.value"
                :value="option.value"
                :aria-describedby="ariaDescribedby"
                name="flavour-3a"
              >
                {{ $t(option.text) }}
              </b-form-checkbox>
              <b-form-invalid-feedback :state="state">{{
                $t('select.user.role.required')
              }}</b-form-invalid-feedback>
            </b-form-group>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block">
              {{ $t('signup') }}
            </button>
          </div>
        </div>
      </form>

      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import User from '../models/user';
import UserService from '../services/user.service';

export default {
  name: 'CreateAccount',
  data() {
    return {
      user: new User('', '', '', '', '', '', ''),
      selectedRoles: [],
      options: [
        { text: 'role.admin', value: 'admin' },
        { text: 'role.manager', value: 'manager' },
        { text: 'role.client', value: 'client' },
      ],
      submitted: false,
      successful: false,
      message: '',
    };
  },
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isAdminInRole() {
      if (this.currentUserAccessLevel == 'ADMIN') {
        return true;
      }
      return false;
    },
    state() {
      if (this.selectedRoles.length >= 1) {
        return true;
      } else {
        return false;
      }
    },
  },
  mounted() {
    if (this.isAdminInRole == false) {
      this.$router.push('/home');
    }
  },
  methods: {
    handleCreate() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          UserService.createUser(this.user, this.selectedRoles).then(
            (data) => {
              this.message = data.message;
              this.successful = true;
              this.$router.push('/home');
            },
            (error) => {
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
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