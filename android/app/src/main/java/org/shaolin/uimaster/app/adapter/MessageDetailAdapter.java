package org.shaolin.uimaster.app.adapter;


import org.shaolin.uimaster.app.context.AppContext;
import org.shaolin.uimaster.app.R;
import org.shaolin.uimaster.app.base.ListBaseAdapter;
import org.shaolin.uimaster.app.bean.Comment;
import org.shaolin.uimaster.app.emoji.InputHelper;
import org.shaolin.uimaster.app.util.StringUtils;
import org.shaolin.uimaster.app.widget.AvatarView;
import org.shaolin.uimaster.app.widget.MyLinkMovementMethod;
import org.shaolin.uimaster.app.widget.MyURLSpan;
import org.shaolin.uimaster.app.widget.TweetTextView;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageDetailAdapter extends ListBaseAdapter<Comment> {

    @Override
    protected boolean loadMoreHasBg() {
        return false;
    }

    @Override
    protected View getRealView(int position, View convertView,
            final ViewGroup parent) {
        final Comment item = mDatas.get(mDatas.size() - position - 1);
        int itemType = 0;
        if (item.getAuthorId() == AppContext.getInstance().getLoginUid()) {
            itemType = 1;
        }
        boolean needCreateView = false;
        ViewHolder vh = null;
        if (convertView == null) {
            needCreateView = true;
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (vh != null && (vh.type != itemType)) {
            needCreateView = true;
        }
        if (vh == null) {
            needCreateView = true;
        }

        if (needCreateView) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    itemType == 0 ? R.layout.list_cell_chat_from
                            : R.layout.list_cell_chat_to, null);
            vh = new ViewHolder(convertView);
            vh.type = itemType;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // vh.name.setText(item.getAuthor());

        vh.content.setMovementMethod(MyLinkMovementMethod.a());
        vh.content.setFocusable(false);
        vh.content.setDispatchToParent(true);
        vh.content.setLongClickable(false);
        Spanned span = Html.fromHtml(item.getContent());
        span = InputHelper.displayEmoji(parent.getResources(), span);
        vh.content.setText(span);
        MyURLSpan.parseLinkText(vh.content, span);

        vh.avatar.setAvatarUrl(item.getPortrait());
        vh.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());

        vh.time.setText(StringUtils.friendly_time(item.getPubDate()));

        return convertView;
    }

    static class ViewHolder {
        int type;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_content)
        TweetTextView content;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
