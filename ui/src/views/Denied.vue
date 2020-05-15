<template>
  <div id="denied" class="w-full">
    <div class="main flex justify-center">
      <font-awesome-icon icon="user-lock"></font-awesome-icon>
      <div class="title">Access denied</div>
    </div>
    <div class="content">
      You do not have access to the application: please contact the HR department if needed.
    </div>
  </div>
</template>

<style lang="scss" scoped>
#denied {
  padding-top: 150px;
  .main {
    font-size: 1.5rem;
    padding-bottom: 10px;
    svg {
      color: #227892;
    }
    .title {
      padding-top: 3px;
      padding-left: 5px;
      font-size: 1.2rem;
    }
  }
}
</style>
