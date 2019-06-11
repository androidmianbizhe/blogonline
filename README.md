# blogonline
该应用采用jobschedule框架，主要用于抓取csdn等热门博客上面的技术文章，方面开发者们阅读，可以随时随地学到技术方面的知识。
同时也会根据用户的阅读某种类型文章的频率，来周期性的为用户推荐适合他的文章，对热爱技术的开发者们有很大的帮助。
应用的功能模块应包括博客数据获取模块、博客数据存储模块、用户数据采集模块、用户行为分析模块、UI模块以及旧数据清理模块

接下来介绍这些模块的详细功能：
博客数据获取模块：在客户端设计多个版块来显示博客信息，其中一栏为推荐版块，用于获取服务端分析用户数据后返回的推荐博客信息。
另外的栏目为对应博客数据源相应的模块，这些栏目可直接抓取csdn和博客园等上的博客信息，解析后显示网络数据。

博客数据存储模块：用户在没有网络的情况下，为了保证用户可以阅读上一次加载的部分博客数据，本地需要对上次访问的博客信息进行缓存。
每次在没有网络的情况下就读取缓存里面的内容，在有网络的情况下，则访问网络拉取数据，同时将下拉刷新获取到的数据内容存储到缓存中，
如果缓存中已经有了相同的数据，则不插入。

用户数据采集模块：为了让后台服务器能对用户的行为做出合适的分析，客户端本地采集用户点击的博客类型，以及点击相关博客类型的次数，
通过后台任务的方式不定时传给服务器，完成数据的采集，也不会影响到用户的操作。本地可以使用文件的方式来存储这些简单的数据。

用户行为分析模块：用户的行为分析将由服务器来完成，服务器将从客户端收集过来的数据进行合理的算法分析，来判断用户更喜欢的博客类型，
从而在推荐的过程中返回更多的该类型的文章。

UI模块：设计出简洁美观的界面用于显示博客信息和博客详情，同时支持用户手势的上拉加载和下拉刷新操作。

旧数据清理模块：客户端本地会不断的缓存博客的相关信息，以及产生一些垃圾文件，这里设置一个后台任务每几天清理一次缓存信息和垃圾文件，
用于保证应用运行的流畅。

用户注册登录模块：用户可以通过手机号验证码注册账号，也可以通过qq，百度，新浪微博等第三方帐号登录此软件。

用户信息查看修改模块：用户可以通过登录帐号后进行头像上传，发布签名，修改性别等用户信息操作。
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/QQ%E7%AC%AC%E4%B8%89%E6%96%B9%E7%99%BB%E5%BD%95.jpg" width="200" alt="第三方登录" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E4%BE%A7%E8%BE%B9%E6%A0%8F.jpg" width="200" alt="侧边栏" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E5%8D%9A%E5%AE%A2%E6%8E%A8%E8%8D%90.jpg" width="200" alt="博客推荐" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E5%8D%9A%E5%AE%A2%E8%AF%A6%E6%83%85.jpg" width="200" alt="博客详情" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E7%9F%AD%E4%BF%A1%E6%B3%A8%E5%86%8C.jpg" width="200" alt="短信注册" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E7%94%A8%E6%88%B7%E8%AF%A6%E6%83%85.jpg" width="200" alt="用户详情" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E9%80%89%E6%8B%A9%E5%8D%9A%E5%AE%A2%E5%B9%B3%E5%8F%B0.jpg" width="200" alt="博客平台" />
<img src="https://github.com/androidmianbizhe/blogonline/blob/master/screen_cut/%E9%80%89%E6%8B%A9%E5%8D%9A%E5%AE%A2%E7%B1%BB%E5%9E%8B.jpg" width="200" alt="博客类型" />

