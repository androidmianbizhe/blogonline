package com.example.lihao.blogeronline.ui;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.UserPrefer;
import com.example.lihao.blogeronline.data.AppRepository;
import com.example.lihao.blogeronline.data.model.UploadArticle;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.http.NetWork;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lihao on 17-11-23.
 */

public class BlogDetailActivity extends BaseActivity {


    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.layout_empty)
    View pageEmpty;
    @BindView(R.id.layout_error)
    View pageError;
    @BindView(R.id.layout_loading)
    View pageLoading;

    private UploadArticle uploadArticle;
    private boolean isRecommend = false;
    private String base_url = "";

    String html_head = "<!doctype html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "</head>\n" +
            "<body>";

    String html_foot = "</body></html>";
    private String html;

    @Override
    protected void doBeforeSetContentView() {
        super.doBeforeSetContentView();

        uploadArticle = getIntent().getParcelableExtra("uploadArticle");
        isRecommend = getIntent().getBooleanExtra("isRecommend",false);
        base_url = SPUtils.getSharedStringData(UIUtils.getContext(),"base_url",Constants.CSDN_URL);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_blog_detail;
    }

    @Override
    protected void initView() {

        if(uploadArticle == null){
            ToastUtils.showToast(getApplicationContext(),"链接异常");
            return;
        }

        //WebSettings webSettings = webView.getSettings();//获取webview设置属性
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        //webSettings.setJavaScriptEnabled(true);//支持js
        //webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        //webSettings.setSupportZoom(true); // 可以缩放

        //===============
        // 设置不可缩放
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDisplayZoomControls(false);

        // 设置支持屏幕适配
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new HTMLWidthInterface(), "HTMLWidth");
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                webView.loadUrl("javascript:window.HTMLWidth.getContentWidth(document.body.offsetWidth);");
//
//            }
//        });

        //========================

        final String replace = uploadArticle.getArticle_link().replace(base_url, "");

        Observable<String> detail = NetWork.getInstance().getApiByHost(this,base_url).getBlogerDetail(replace);
        detail.map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                //解析
                return dealhtml(s);
            }
        }).doOnNext(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {

                if(!isRecommend){
                    //如果已经登陆则上传偏好
                    UserManager userManager = UserManager.create(UIUtils.getContext());
                    if(userManager.getIsLogin()){
                        //增加次数
                        String userPrefer = SPUtils.getSharedStringData(UIUtils.getContext(),"UserPrefer","");
                        Gson gson = new Gson();
                        UserPrefer prefer = gson.fromJson(userPrefer,
                                new TypeToken<UserPrefer>() {}.getType());

                        boolean isNewData = false;

                        if("".equals(userPrefer) || prefer == null){
                            return;
                        }
                        for(int i=0;i<prefer.getPrefer().size();i++){
                            UserPrefer.Prefer pre = prefer.getPrefer().get(i);
                            if(pre.getType_id() == uploadArticle.getArticle_type_id()
                                    && pre.getFrom_id() == uploadArticle.getArticle_from_id()){
                                isNewData = true;
                                prefer.getPrefer().get(i).setViewCount(pre.getViewCount()+1);
                            }
                        }
                        //写入新数据
                        if(!isNewData){
                            prefer.getPrefer()
                                    .add(new UserPrefer
                                            .Prefer(uploadArticle.getArticle_type_id(),
                                            1,uploadArticle.getArticle_from_id()));
                        }

                        //储存数据
                        Gson gsons = new Gson();
                        String json = gsons.toJson(prefer);
                        SPUtils.setSharedStringData(UIUtils.getContext(),"UserPrefer",json);
                    }

                    ArrayList<UploadArticle> articles = new ArrayList<>();
                    articles.add(uploadArticle);
                    //保存数据
                    AppRepository.getInstance().insert(articles)
                            .compose(MyNetwork.schedulersTransformer)
                            .subscribe(new Observer<List<UploadArticle>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(List<UploadArticle> articleList) {
                                    Log.e("插入数据","成功");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("插入数据","失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        }).compose(NetWork.schedulersTransformer)
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        pageLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Object o) {
                        html = o.toString();
                        Log.e("onNext","===");
                        if("".equals(html) || html == null){
                            pageLoading.setVisibility(View.GONE);
                            pageEmpty.setVisibility(View.VISIBLE);
                        }else {

                            pageLoading.setVisibility(View.GONE);
                            webView.setVisibility(View.VISIBLE);

                            //加载本地网页
                            webView.loadDataWithBaseURL(null,html_head+html+html_foot, "text/html", "utf-8", null);
                            //webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                        pageLoading.setVisibility(View.GONE);
                        pageError.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private String dealhtml(String htmltext) {

        //article_content 内容

        //article_details 题目
        //Document doc = Jsoup.parse(s);
        //Element content = doc.getElementById("article_content");
        //return content.html();

        String body = SPUtils.getSharedStringData(this,"article_body","");

        try {
            Document doc= Jsoup.parse(htmltext);
            Elements elements=doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width","100%").attr("height","auto");
            }

            if(!body.equals("")){
                Element element = doc.getElementById(body);

                return element.html();
            }

            return doc.toString();
        } catch (Exception e) {

            return htmltext;
        }
    }

    @Override
    protected void setPresenter() {

    }


    class HTMLWidthInterface {
        @JavascriptInterface
        public void getContentWidth(String value,String path) {
            if (value != null) {
                int bodyWidth = Integer.parseInt(value);
                boolean hasChanged = isContainsText(html, "<meta name=\"viewport\"");
                if(!hasChanged) {
                    htmlAdjustWithPageWidth(bodyWidth, path, webView);
                    webView.loadUrl("file://" + path);
                }
            }
        }
    }



    private void htmlAdjustWithPageWidth(float bodyWidth, String html, WebView webView) {
        // 计算要缩放的比例
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度
        float scale = webView.getMeasuredWidth() / density / bodyWidth;
        String insertText = "<meta name=\"viewport\" content=\"width="+ bodyWidth +"px initial-scale="+ scale
                + ", minimum-scale=0.1, maximum-scale=10.0, user-scalable=no\"/>";
        insertBehindText(new File(html), "<head>", insertText);
    }

    /**
     * 在指定文本之后插入一段文本
     *
     * @param file
     * @param targetStr
     * @param insertStr
     * @return void:
     * @version 1.0
     * @date 2015-9-2
     * @Author zhou.wenkai
     */
    public void insertBehindText(File file, String targetStr, String insertStr) {
        BufferedReader bufr = null;
        BufferedWriter bufw = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufr = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            String line = null;

            while ((line = bufr.readLine()) != null) {
                // 保存原文本
                buf = buf.append(line);
                // 保存要插入文本
                if(line.contains(targetStr)) {
                    buf.append(insertStr);
                }
                // 添加换行符号
                buf = buf.append(System.getProperty("line.separator"));
            }
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            bufw = new BufferedWriter(osw);
            bufw.write(buf.toString());
            bufw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufr != null) {
                    bufr.close();
                }
                if(bufw != null) {
                    bufw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查是否包含制定文本
     *
     * @param containsStr
     * @return
     * @return boolean:
     * @version 1.0
     * @date 2015-10-28
     * @Author zhou.wenkai
     */
    public boolean isContainsText(String html, String containsStr) {
        return html.contains(containsStr);
    }


}
