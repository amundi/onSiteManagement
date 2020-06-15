<template>
  <div id="leftMenu" class="select-none" :class="[hue]">
    <div class="user-menu" v-if="user">
      <div class="item" :class="{ selected: currentRoute === '/' }">
        <router-link to="/" class="nav-link w-auto">
          <font-awesome-icon icon="chalkboard"></font-awesome-icon>
          <span>Home</span>
        </router-link>
      </div>
    </div>
    <div class="admin-menu" v-if="user != null && user.role == 'admin' && overridenUser == null">
      <div class="title">Admin</div>
      <div class="item" :class="{ selected: currentRoute === '/settings' }">
        <router-link to="/settings" class="nav-link w-auto">
          <font-awesome-icon icon="cogs"></font-awesome-icon>
          <span>Settings</span>
        </router-link>
      </div>
      <div class="item" :class="{ selected: currentRoute === '/sites' }">
        <router-link to="/sites" class="nav-link w-auto">
          <font-awesome-icon icon="city"></font-awesome-icon>
          <span>Sites</span>
        </router-link>
      </div>
      <div class="item" :class="{ selected: currentRoute === '/restaurants' }">
        <router-link to="/restaurants" class="nav-link w-auto">
          <font-awesome-icon icon="utensils"></font-awesome-icon>
          <span>Restaurants</span>
        </router-link>
      </div>
       <div class="item" :class="{ selected: currentRoute === '/parkings' }">
        <router-link to="/parkings" class="nav-link w-auto">
          <font-awesome-icon icon="parking"></font-awesome-icon>
          <span>Parkings</span>
        </router-link>
      </div>
    </div>
    <div
      class="report-menu"
      v-if="
        user != null && (user.role == 'admin' || user.role == 'reporter') && overridenUser == null
      "
    >
      <div class="title">Reporting</div>
      <div class="item" :class="{ selected: currentRoute === '/reports' }">
        <router-link to="/reports" class="nav-link w-auto">
          <font-awesome-icon icon="poll"></font-awesome-icon>
          <span>General</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["hue", "user", "overridenUser"])
  },
  data() {
    return {
      currentRoute: "/"
    };
  },
  watch: {
    $route(to) {
      this.currentRoute = to.path;
    }
  },
  mounted() {
    this.currentRoute = this.$route.path;
  }
};
</script>

<style lang="scss" scoped>
@import "@/scss/_variables.scss";

#leftMenu {
  width: 150px;
  min-width: 150px;
  height: 100vh;
  padding-top: 100px;
  border-right: 1px solid;
  text-align: left;
  font-size: 0.8rem;
  font-weight: bold;

  &.dark {
    border-color: #464646;
    .title {
      color: #bfbfbf;
      border-bottom: 1px solid #464646;
    }
    .item.selected {
      background-color: #2d2d2d;
    }
  }

  &.light {
    border-color: #cacaca;
    .title {
      color: #6d6d6d;
      border-bottom: 1px solid #cacaca;
    }
    .item.selected {
      background-color: #f1f1f1;
    }
  }

  .admin-menu,
  .report-menu {
    padding-top: 50px;
    margin-bottom: 40px;
  }

  .title {
    font-size: 1rem;
    font-weight: 100;
    margin-bottom: 15px;
  }

  .item {
    margin: 5px 0;
    padding: 5px;
    a {
      display: inline-block;
      width: 100%;
    }
    span {
      padding-left: 10px;
      &.edit {
        padding-left: 14px;
      }
    }

    &.selected {
      border-right: 3px solid $primaryColor;
    }
  }
}
</style>
