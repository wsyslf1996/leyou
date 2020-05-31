const lyLeft = {
    template: " \
    <div class='nav-left'>\
        <div class='person-info'> \
            <div class='person-photo'><img src='img/_/photo.png' alt=''></div>\
                <div class='person-account'>\
                    <span class='name'>Michelle</span>\
                    <span class='safe'><a href='#'>退出登录 </a></span>\
                </div>\
                <div class='clearfix'>\
            </div>\
        </div>\
        <div class='list-items'>\
            <dl>\
                <dt><i>·</i> 订单中心</dt>\
                <dd ><a href='home-index.html' key='1-1':class='key===activeindex?list-active:\"\"'>我的订单</a></dd>\
                <dd><a href='home-order-pay.html' :key='1-2' :class='key===activeindex?list-active:\"\"'>待付款</a></dd>\
                <dd><a href='home-order-send.html' :key='1-3' :class='key===activeindex?list-active:\"\"'>待发货</a></dd>\
                <dd><a href='home-order-receive.html' :key='1-4' :class='key===activeindex?list-active:\"\"'>待收货</a></dd>\
                <dd><a href='home-order-evaluate.html' :key='1-5' :class='key===activeindex?list-active:\"\"'>待评价</a></dd>\
            </dl>\
            <dl>\
                <dt><i>·</i> 我的中心</dt>\
                <dd><a href='home-person-collect.html'>我的收藏</a></dd>\
                <dd><a href='home-person-footmark.html'>我的足迹</a></dd>\
            </dl>\
            <dl>\
                <dt><i>·</i> 物流消息</dt>\
            </dl>\
            <dl>\
                <dt><i>·</i> 设置</dt>\
                <dd><a href='home-setting-info.html'>个人信息</a></dd>\
                <dd><a href='home-setting-address.html'  >地址管理</a></dd>\
                <dd><a href='home-setting-safe.html' >安全管理</a></dd>\
            </dl>\
        </div> \
    </div>\
    ",
    name:'ly-left',
    data() {
        return {
            key: "",
            query: location.search,
            cartNums: 0
        }
    },
    methods: {
    },
    props:{
        activeindex: String,
    }
}
export default lyLeft;