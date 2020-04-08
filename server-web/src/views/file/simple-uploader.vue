<template>
  <uploader ref="simple-upload" :options="simpleConfig" class="uploader-example" @file-added="fileAdded" @file-success="fileSuccess">
    <uploader-unsupport/>
    <uploader-btn>选择文件</uploader-btn>
    <uploader-list/>
  </uploader>
</template>
<script>
import { getToken } from '@/util/auth'
import baseUrl from '@/config'
const CODE_OK = 0
export default {
  props: {
    fileDirectoryId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
    }
  },
  computed: {
    simpleConfig() {
      return {
        target: baseUrl + 'web/file/simpleUpload',
        chunkSize: 5 * 1024 * 1024,
        simultaneousUploads: 1,
        forceChunkSize: true,
        query: {
          directoryId: this.fileDirectoryId
        },
        headers: {
          token: getToken()
        },
        testChunks: false,
        processParams(params, file) {
          if (file.fileType.indexOf('image') > -1) {
            params.fileType = 1
          } else if (file.fileType.indexOf('video') > -1){
            params.fileType = 2
          } else {
            return false
          }
          return params
        }
      }
    }
  },
  methods: {
    // 文件添加的事件
    fileAdded(file) {
      if (file.fileType.indexOf('image') > -1) {
        return true
      } else if (file.fileType.indexOf('video') > -1){
        return true
      } else {
        // 移除上传的文件
        this.$refs['simple-upload'].uploader.removeFile(file)
        this.$message.error('文件格式不允许')
        return false
      }
    },
    // 上传成功的事件
    fileSuccess(rootFile, file, message) {
      const res = JSON.parse(message)
      if (res.code === CODE_OK) {
        this.$notify({
          title: 'success',
          message: res.msg,
          type: 'success',
          duration: 2000
        })
        const uploader = this.$refs['simple-upload'].uploader
        const fileList = uploader.fileList
        this.$refs['simple-upload'].fileList.splice(fileList.findIndex(item => rootFile === item), 1)
      } else {
        this.$message.error({ message: res.msg })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.uploader-example {
    padding: 15px;
    margin: 20px auto 0;
    font-size: 12px; 
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
    .uploader-btn {
      margin-right: 4px;
    }
    .uploader-list {
      margin-top: 10px;
      max-height: 440px;
      overflow: auto;
      overflow-x: hidden;
      overflow-y: auto;
    }
  }
</style>
