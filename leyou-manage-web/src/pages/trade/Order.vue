<template>
  <v-card>
    <v-card-title>
      <v-btn color="primary" @click="addBrand">新增品牌</v-btn>
      <!--搜索框，与search属性关联-->
      <v-spacer/>
      <v-flex xs3>
        <v-text-field label="输入关键字搜索" v-model.lazy="search" append-icon="search" hide-details/>
      </v-flex>
    </v-card-title>
    <v-divider/>
    <v-data-table
      :headers="headers"
      :items="orders"
      :pagination.sync="pagination"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.orderId }}</td>
        <td class="text-xs-center">¥{{ props.item.totalValue }}</td>
        <td class="text-xs-center red--text">{{ props.item.orderStatus }}</td>
        <td class="text-xs-center">{{ props.item.receiver }}</td>
        <td class="text-xs-center"><a @click="editO">订单详情</a></td>
      </template>
    </v-data-table>
    <v-dialog max-width="500" v-model="show" persistent scrollable>
      <v-card>
        <!--对话框的标题-->
        <v-toolbar dense dark color="primary">
          <v-toolbar-title>{{isEdit ? '修改' : '新增'}}品牌</v-toolbar-title>
          <v-spacer/>
          <!--关闭窗口的按钮-->
          <v-btn icon @click="closeWindow"><v-icon>close</v-icon></v-btn>
        </v-toolbar>
        <!--对话框的内容，表单-->
<!--        <v-card-text class="px-5" style="height:400px">-->
<!--          <brand-form @close="closeWindow" :oldBrand="oldBrand" :isEdit="isEdit"/>-->
<!--        </v-card-text>-->
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
    export default {
        name: "order",
        data() {
            return {
                search: '', // 搜索过滤字段
                totalBrands: 0, // 总条数
                brands: [], // 当前页品牌数据
                loading: true, // 是否在加载中
                pagination: {}, // 分页信息
                headers: [
                    { text: '订单号', align: 'center', value: 'orderId', },
                    { text: '订单金额', align: 'center', value: 'totalValue' },
                    { text: '订单状态', align: 'center', value: 'orderStatus' },
                    { text: '收货人信息', align: 'center', value: 'receiver' },
                    { text: '', align: 'center', value: 'detail',sortable:false },
                ],
                orders:[{
                    orderId:'1264082905118937088',
                    totalValue: '5500.00',
                    orderStatus: '已发货',
                    receiver : '重庆市永川区科园路9号附20号 15922871526',
                }],
                show: false,// 控制对话框的显示
                oldBrand: {}, // 即将被编辑的品牌数据
                isEdit: false, // 是否是编辑
            }
        },
        mounted() { // 渲染后执行
            // 查询数据
            // this.getDataFromServer();
        },
        watch: {
            pagination: { // 监视pagination属性的变化
                deep: true, // deep为true，会监视pagination的属性及属性中的对象属性变化
                handler() {
                    // 变化后的回调函数，这里我们再次调用getDataFromServer即可
                    this.getDataFromServer();
                }
            },
            search: { // 监视搜索字段
                handler() {
                    this.getDataFromServer();
                }
            }
        },
        methods: {
            editO(){this.show=true},
            getDataFromServer() { // 从服务的加载数的方法。
                // 发起请求
                // this.$http.get("/item/brand/page", {
                //     params: {
                //         key: this.search, // 搜索条件
                //         page: this.pagination.page,// 当前页
                //         rows: this.pagination.rowsPerPage,// 每页大小
                //         sortBy: this.pagination.sortBy,// 排序字段
                //         desc: this.pagination.descending// 是否降序
                //     }
                // }).then(resp => { // 这里使用箭头函数
                //     this.brands = resp.data.items;
                //     this.totalBrands = resp.data.total;
                //     // 完成赋值后，把加载状态赋值为false
                //     this.loading = false;
                // })
            },
            addBrand() {
                // 修改标记
                this.isEdit = false;
                // 控制弹窗可见：
                this.show = true;
                // 把oldBrand变为null
                this.oldBrand = null;
            },
            editBrand(oldBrand){
                // 根据品牌信息查询商品分类
                this.$http.get("/item/category/bid/" + oldBrand.id)
                    .then(({data}) => {
                        // 修改标记
                        this.isEdit = true;
                        // 控制弹窗可见：
                        this.show = true;
                        // 获取要编辑的brand
                        this.oldBrand = oldBrand
                        // 回显商品分类
                        this.oldBrand.categories = data;
                    })
            },
            deleteBrand(brand){
                this.$message.confirm('此操作将永久删除数据，是否继续?', '提示', {
                    confirmButtonText: '确定删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.delete("/item/brand/" + brand.id)
                        .then(() => {
                            console.log("delete ... " + brand.id);
                            this.$message.success("删除成功");
                            this.getDataFromServer();
                        })
                }).catch(()=>{
                    this.$message.info('已取消删除');
                })
            },
            closeWindow(){
                // 重新加载数据
                this.getDataFromServer();
                // 关闭窗口
                this.show = false;
            }
        },
    }
</script>

<style scoped>

</style>
