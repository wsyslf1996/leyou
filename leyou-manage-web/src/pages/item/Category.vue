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
                tipsTitle: "分类名称：",
                category: {
                    id: '',
                    name: '',
                    parentId: '',
                    isParent: '',
                    sort: ''
                },
            }
        },
        methods: {
            handleAdd(node) {
                console.log("add .... ");
                this.isEdit = false;
                this.category = node;
                this.$http({
                    method: this.isEdit ? 'put' : 'post',
                    url: '/item/category',
                    data: this.$qs.stringify(this.category)
                }).then(() => {
                    this.$message.success("新增成功！");
                }).catch(() => {
                    this.$message.error("新增失败！");
                });
                this.isEdit = true;
                console.log(node);
            },
            handleEdit(id, name) {
                console.log("edit... id: " + id + ", name: " + name);
                this.category.id = id;
                this.category.name = name;
                this.category.parentId = '';
                this.category.isParent = '';
                this.category.sort = '';
                this.$http({
                    method: this.isEdit ? 'put' : 'post',
                    url: '/item/category',
                    data: this.$qs.stringify(this.category)
                }).then(() => {
                    this.$message.success("保存成功！");
                }).catch(() => {
                    this.$message.error("保存失败！");
                });
            },
            handleDelete(id) {
                this.$http.delete("/item/category/" + id)
                    .then(() => {
                        this.$message.success("删除成功");
                        console.log("delete ... " + id)
                    })
            },
            handleClick(node) {
                console.log(node)
            }
        }
    };
</script>

<style scoped>

</style>
