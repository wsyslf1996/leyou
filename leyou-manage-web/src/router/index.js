import Vue from 'vue'
import Router from 'vue-router'
import VueCookies from 'vue-cookies'

Vue.use(Router)
Vue.use(VueCookies)

function route (path, file, name, children,requireAuth) {
  return {
    exact: true,
    path,
    name,
    children,
    meta :{
      requireAuth: requireAuth
    },
    component: () => import('../pages' + file)
  }
}

const router = new Router({
  routes: [
    route("/login",'/Login',"Login",null,false),// /login路径，路由到登录组件
    {
      path:"/", // 根路径，路由到 Layout组件
      component: () => import('../pages/Layout'),
      redirect:"/index/dashboard",
      children:[ // 其它所有组件都是 Layout的子组件
        route("/index/dashboard","/Dashboard","Dashboard",null,true),
        route("/item/category",'/item/Category',"Category",null,false),
        route("/item/brand",'/item/Brand',"Brand",null,true),
        route("/item/list",'/item/Goods',"Goods",null,true),
        route("/item/specification",'/item/specification/Specification',"Specification",null,true),
        route("/user/statistics",'/user/Statistics',"Statistics",null,true),
        route("/trade/promotion",'/trade/Promotion',"Promotion",null,true)
      ]
    }
  ]
})

export default router

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
    if (sessionStorage.getItem("t") == 'true') { // 判断本地是否存在token
      console.log("我登录了");
      next()
    } else {
      // 未登录,跳转到登陆页面
      console.log("未登录");
      next({
        path: '/login'
      })
    }
  } else {
    console.log("不要求登录");
    next();
    // if(sessionStorage.getItem("t") == 'true'){
    //   next();
    // }else{
    //   next();
    // }
  }
});
