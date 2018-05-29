package com.example.lihao.blogeronline.data;

import android.content.Context;

import com.example.lihao.blogeronline.data.model.UploadArticle;
import com.example.lihao.blogeronline.service.UploadArticleJobService;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by lihao on 17-11-27.
 */

public class UploadArticleDaoImple implements UploadArticleDao {

    private Context mContext;
    private LiteOrm mLiteOrm;

    public UploadArticleDaoImple(Context context,LiteOrm liteOrm){
        mContext = context;
        mLiteOrm = liteOrm;
    }

    @Override
    public Observable<List<UploadArticle>> insert(final List<UploadArticle> articleList) {
        return Observable.create(new ObservableOnSubscribe<List<UploadArticle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UploadArticle>> e) throws Exception {

                for(UploadArticle a :articleList){
                    mLiteOrm.insert(a, ConflictAlgorithm.Abort);
                }

                e.onNext(articleList);
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<UploadArticle> update(final UploadArticle article) {
        return Observable.create(new ObservableOnSubscribe<UploadArticle>() {
            @Override
            public void subscribe(ObservableEmitter<UploadArticle> e) throws Exception {
                mLiteOrm.delete(article);

                long result = mLiteOrm.insert(article);
                if (result > 0) {
                    e.onNext(article);
                } else {
                    e.onError(new Exception("update article failed"));
                }
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<List<UploadArticle>> delete(final List<UploadArticle> articles) {
        return Observable.create(new ObservableOnSubscribe<List<UploadArticle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UploadArticle>> e) throws Exception {

                for (int i=0;i<articles.size();i++){

                    mLiteOrm.delete(articles.get(i));
                }

                e.onNext(articles);
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<List<UploadArticle>> select(final int size) {
        return Observable.create(new ObservableOnSubscribe<List<UploadArticle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UploadArticle>> e) throws Exception {

                QueryBuilder<UploadArticle> qb =
                        new QueryBuilder<UploadArticle>(UploadArticle.class)
                        .limit(0, UploadArticleJobService.QUERY_LENGTH);

                ArrayList<UploadArticle> query = mLiteOrm.query(qb);
                if(query == null){
                    e.onError(new Throwable("query failed"));
                }else {
                    e.onNext(query);
                }
                e.onComplete();

            }
        });
    }
}
