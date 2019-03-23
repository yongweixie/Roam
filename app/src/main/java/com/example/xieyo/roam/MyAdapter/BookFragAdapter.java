package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.willy.ratingbar.BaseRatingBar;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BookFragAdapter extends BaseMultiItemQuickAdapter<BookFragList, BaseViewHolder> {
    public BookFragAdapter(@Nullable List<BookFragList> data) {
        super(data);

        addItemType(1, R.layout.book_frag_title);
        addItemType(2, R.layout.book_frag_nostar);
        addItemType(3, R.layout.book_frag_withstar);
        addItemType(4, R.layout.book_best_sell);
        addItemType(5, R.layout.book_search_layout);
        addItemType(6, R.layout.book_digest);


    }

    @Override
    protected void convert(BaseViewHolder helper, BookFragList item) {

        ImageView coverview;
        BaseRatingBar ratingBar;
        RequestOptions options = new RequestOptions()
                .optionalTransform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        //.override(200,285)
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

        switch (helper.getItemViewType()) {
            case 1:
                if (item.from == 1) {
                    helper.setText(R.id.tv_content, "新书速递");
                }
                if (item.from == 2) {
                    helper.setText(R.id.tv_content, "最受关注图书榜");
                }
                if (item.from == 3) {
                    helper.setText(R.id.tv_content, "豆瓣250");
                }
                if (item.from == 4) {
                    helper.setText(R.id.tv_content, "京东畅销榜");
                }
                if (item.from == 5) {
                    helper.setText(R.id.tv_content, "当当畅销网");
                }
                break;
            case 2:
                helper.setText(R.id.book_name, item.name)
                        .setText(R.id.book_artist, item.artist);
                coverview = helper.getView(R.id.book_cover);


                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);
                break;
            case 3:
                helper.setText(R.id.book_name, item.name)
                        .setText(R.id.book_artist, item.artist)
                        .setText(R.id.book_classification, item.classification)
                        .setText(R.id.book_reviews, item.reviews)
                        .setText(R.id.book_rating, item.rating);
                coverview = helper.getView(R.id.book_cover);

                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);


                 ratingBar = helper.getView(R.id.simpleRatingBar);

                // ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
                if (item.rating.length() == 3) {
                    ratingBar.setRating(Float.valueOf(item.rating) / 2);
                }
                break;
            case 4:
                helper.setText(R.id.sell_index, item.sellindex)
                        .setText(R.id.book_artist, item.artist)
                        .setText(R.id.book_title, item.name);
                break;
            case 5:
                helper.setText(R.id.book_name, item.name)
                        .setText(R.id.book_artist, item.artist)
                        .setText(R.id.numraters, "("+item.numraters+"评价)")
                        .setText(R.id.book_rating, item.rating);
                coverview = helper.getView(R.id.book_cover);

                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);

                 ratingBar = helper.getView(R.id.simpleRatingBar);

                // ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
                if (item.rating.length() == 3) {
                    ratingBar.setRating(Float.valueOf(item.rating) / 2);
                }
                break;
            case 6:
                helper.setText(R.id.book_name, item.name)
                        .setText(R.id.part_digest, item.part_digest)
                        .setText(R.id.num_digest, item.num_digest);
                coverview = helper.getView(R.id.book_cover);

                Glide.with(mContext).load(item.coveruri)
                        .apply(options)
                        .into(coverview);
                break;
            default:
                break;

        }

    }

}
