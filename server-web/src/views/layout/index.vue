<template>
  <div class="app-wrapper">
    <el-container>
      <el-aside width="200px">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="1" @click="toPath('/dashboard')">
            <i class="el-icon-menu"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="2" @click="toPath('/agent')">
            <i class="el-icon-setting"></i>
            <span slot="title">终端信息</span>
          </el-menu-item>
          <el-menu-item index="3" @click="toPath('/file')">
            <i class="el-icon-setting"></i>
            <span slot="title">素材信息</span>
          </el-menu-item>
          <el-menu-item index="4" @click="toPath('/article')">
            <i class="el-icon-setting"></i>
            <span slot="title">文稿信息</span>
          </el-menu-item>
          <el-menu-item index="5" @click="toPath('/agent-playlist')">
            <i class="el-icon-setting"></i>
            <span slot="title">播单信息</span>
          </el-menu-item>
          <el-menu-item index="6" @click="toPath('/user')">
            <i class="el-icon-setting"></i>
            <span slot="title">用户信息</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <h1>信息发布系统</h1>
          <span class="user-name">当前用户：{{ userName }}</span>
        </el-header>
        <section class="app-main">
          <router-view/>
        </section>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  components: {
  },
  data() {
    return {
      currentPath: '/dashboard',
      activeMap: {
        '/dashboard': '1',
        '/agent': '2',
        '/file': '3',
        '/article': '4',
        '/play-list': '5',
        '/user': '6'
      }
    }
  },
  computed: {
    ...mapGetters([
      'userName'
    ]),
    activeMenu() {
      return this.activeMap[this.$route.path]
    }
  },
  methods: {
    // 跳转路由
    toPath(path) {
      if (this.currentPath === path) {
        this.$message.info('当前页面就是目标页面，无需跳转')
        return
      }
      this.currentPath = path
      this.$router.push(path)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  &.mobile.openSidebar{
    position: fixed;
    top: 0;
  }
  .header {
    border-bottom: 1px solid #d0d0d0;
    h1 {
      float: left;
    }
    .user-name {
      line-height: 60px;
      float: right;
    }
  }
  .el-menu-vertical {
    height: 100vh;
  }
}
</style>
