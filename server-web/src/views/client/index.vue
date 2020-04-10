<template>
  <div class="wrap">
    <el-card v-for="item in list" :key="item.listId">
      <h1>{{ item.title }}</h1>
      <img v-show="item.coverImage && item.coverImage.length > 0" :src="item.coverImage">
      <div v-html="item.content"/>
    </el-card>
  </div>
</template>
<script>
import { heartbeat } from '@/api/agent'
import { getAgentPlaylistByAgentId } from '@/api/agent-playlist'
export default {
  data() {
    return {
      agentId: this.$route.query.agentId || '',
      heartbeatFrequency: this.$route.query.heartbeatFrequency || 60,
      timer: null,
      list: []
    }
  },
  created() {
    if (this.agentId.length < 1) {
      this.$message.error('无效的终端')
      return
    }
    this.heartbeat()
    if (this.timer === null) {
      this.timer = setInterval(() => {
        this.heartbeat()
      }, this.heartbeatFrequency * 1000)
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
    this.timer = null
  },
  methods: {
    // 心跳
    heartbeat() {
      heartbeat(this.agentId).then(res => {
        if (res.code === 0) {
          this.getAgentPlaylist()
        }
      })
    },
    // 终端播单列表
    getAgentPlaylist() {
      getAgentPlaylistByAgentId(this.agentId).then(res => {
        if (res.code === 0) {
          this.list = res.data
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.wrap {
  width: 800px;
  margin: 0 auto;
}
</style>