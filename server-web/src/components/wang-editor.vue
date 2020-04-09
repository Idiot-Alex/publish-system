<template>
  <div class="editor">
    <div ref="editor-form"/>
  </div>
</template>
<script>
import E from 'wangeditor'
export default {
  props: {
    content: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 真正的编辑器里的内容
      editorContent: '',
      // 编辑对象
      editor: null
    }
  },
  watch: {
    // 当伏组件内容变化时需要更新编辑器的内容
    content() {
      this.editor.txt.html(this.content)
    }
  },
  mounted() {
    // 初始化
    this.editor = new E(this.$refs['editor-form'])
    // 当编辑器内容变化时通知父组件
    this.editor.customConfig.onchange = (html) => {
      this.editorContent = html
      // 通知父组件方法
      this.$emit('editorContent', html)
    }
    // 配置菜单
    this.editor.customConfig.menus = [
      'head',
      'bold',
      'fontSize',
      'fontName',
      'italic',
      'underline',
      'strikeThrough',
      'foreColor',
      'backColor',
      'link',
      'list',
      'justify',
      'quote',
      'emotion',
      'table',
      'image',
      'video',
      'code',
      'undo',
      'redo'
    ]
    this.editor.create()
    this.editor.txt.html(this.content)
  }
}
</script>