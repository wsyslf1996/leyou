const lyLeft = {
    template: " \
    <div class='nav-left'>\
        <div class='person-info'> \
            <div class='person-photo'><img src='img/_/photo.png' alt=''></div>\
                <div class='person-account'>\
                    <span class='name'>{{user.username}}</span>\
                    <span class='safe'><a href='#' @click='logout'>退出登录 </a></span>\
                </div>\
                <div class='clearfix'>\
            </div>\
        </div>\
        <div class='list-items'>\
            <dl v-for='(menu,i) in menuleft' :key='i'>\
                <dt><i>·</i> {{menu.name}}</dt>\
                <dd v-for='submenu in menu.submenus' :key='submenu.index'>\
                    <a :href='submenu.address' :class='{\"list-active\":submenu.index===activeindex}' v-text='submenu.name'></a>\
                </dd> \
            </dl>\
        </div> \
    </div>\
    ",
    name:'ly-left',
    ly,
    data() {
        return {
            key: "",
            query: location.search,
            cartNums: 0,
            menuleft:[{
                name:"订单中心",
                index: "1",
                submenus:[{
                    name :"我的订单",
                    index: "1-1",
                    address: "home-index.html"
                },{
                    name :"待付款",
                    index: "1-2",
                    address: "home-order-pay.html"
                },{
                    name :"待发货",
                    index: "1-3",
                    address: "home-order-send.html"
                },{
                    name :"待收货",
                    index: "1-4",
                    address: "home-order-receive.html"
                }]
            }]
        }
    },
    methods: {
        logout(){
            Cookies.set("t",null,{domain:".leyou.com",expires: new Date()})
            window.location.href = "http://www.leyou.com";
        }
    },
    props:{
        activeindex: String,
        user :Object,
    }
}
export default lyLeft;