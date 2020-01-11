<template>
  <v-card>
    <v-flex xs12 sm10>
      <v-tree url="/item/category/list"
              :isEdit="isEdit"
              :tipsTitle="tipsTitle"
              @handleAdd="handleAdd"
              @handleEdit="handleEdit"
              @handleDelete="handleDelete"
              @handleClick="handleClick"
      />
    </v-flex>
  </v-card>
</template>

<script>
    export default {
        name: "category",
        data() {
            return {
                isEdit: true,
                tipsTitle:"分类名称：",
                category:{
                    id: '',
                    name: '',
                }
            }
        },
        methods: {
            handleAdd(node) {
                console.log("add .... ");
                console.log(node);
            },
            handleEdit(id, name) {
                console.log("edit... id: " + id + ", name: " + name);
                this.category.id = id;
                this.category.name = name;
                this.$http({
                    method: this.isEdit ? 'put' : 'post',
                    url: '/item/category',
                    data: this.$qs.stringify(this.category)
                }).then(() => {
                    // 关闭窗口
                    // this.show = false;
                    this.$message.success("保存成功！");
                    // this.loadData();
                }).catch(() => {
                    this.$message.error("保存失败！");
                });
            },
            handleDelete(id) {
                console.log("delete ... " + id)
            },
            handleClick(node) {
                console.log(node)
            }
        }
    };
</script>

<style scoped>

</style>
