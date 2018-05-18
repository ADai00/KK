package edu.hnie.kk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.BaseActivity;
import edu.hnie.kk.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<IMMessage> msgList;
    private Context context;

    public MsgAdapter(List<IMMessage> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        TextView leftMsg;
        ImageView leftImg;
        CircleImageView leftHeaderIcon;

        LinearLayout rightLayout;
        TextView rightMsg;
        ImageView rightImg;
        CircleImageView rightHeaderIcon;

        LinearLayout leftFileLayout;
        ImageView leftFileImage;
        TextView leftFileName;
        TextView leftFileSize;
        TextView leftFileStatus;

        LinearLayout rightFileLayout;
        ImageView rightFileImage;
        TextView rightFileName;
        TextView rightFileSize;
        TextView rightFileStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            leftMsg = itemView.findViewById(R.id.left_msg);
            leftImg = itemView.findViewById(R.id.left_img);
            leftHeaderIcon = itemView.findViewById(R.id.left_header_icon);

            rightLayout = itemView.findViewById(R.id.right_layout);
            rightMsg = itemView.findViewById(R.id.right_msg);
            rightImg = itemView.findViewById(R.id.right_img);
            rightHeaderIcon = itemView.findViewById(R.id.right_header_icon);

            leftFileLayout = itemView.findViewById(R.id.left_file_layout);
            leftFileImage = itemView.findViewById(R.id.left_file_image);
            leftFileName = itemView.findViewById(R.id.left_file_name);
            leftFileSize = itemView.findViewById(R.id.left_file_size);
            leftFileStatus = itemView.findViewById(R.id.left_file_status);

            rightFileLayout = itemView.findViewById(R.id.right_file_layout);
            rightFileImage = itemView.findViewById(R.id.right_file_image);
            rightFileName = itemView.findViewById(R.id.right_file_name);
            rightFileSize = itemView.findViewById(R.id.right_file_size);
            rightFileStatus = itemView.findViewById(R.id.right_file_status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msg_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String account = preferences.getString("account", null);

        IMMessage message = msgList.get(i);
        String fromAccount = message.getFromAccount();
        if (message.getMsgType() == MsgTypeEnum.image) {
            if (account.equals(fromAccount)) {
                showSendImageMessage(message, viewHolder);
            } else {
                showReceiveImageMessage(message, viewHolder);
            }
        } else if (message.getMsgType() == MsgTypeEnum.text) {
            if (account.equals(fromAccount)) {
                showSendTextMessage(message, viewHolder);
            } else {
                showReceiveTextMessage(message, viewHolder);
            }
        } else if (message.getMsgType() == MsgTypeEnum.file) {
            if (account.equals(fromAccount)) {
                showSendFileMessage(message, viewHolder);
            } else {
                showReceiveFileMessage(message, viewHolder);
            }
        }

    }


    //=================================接收=======================================

    /**
     * 显示接收到的文本消息
     *
     * @param message
     * @param viewHolder
     */
    private void showReceiveTextMessage(IMMessage message, ViewHolder viewHolder) {
        viewHolder.rightLayout.setVisibility(View.GONE);
        viewHolder.leftLayout.setVisibility(View.VISIBLE);
        viewHolder.leftFileLayout.setVisibility(View.GONE);
        viewHolder.leftImg.setVisibility(View.GONE);
        viewHolder.leftMsg.setText(message.getContent());
    }


    /**
     * 显示接收的图片消息
     *
     * @param message
     * @param viewHolder
     */
    private void showReceiveImageMessage(final IMMessage message, final ViewHolder viewHolder) {
        viewHolder.rightLayout.setVisibility(View.GONE);
        viewHolder.leftLayout.setVisibility(View.GONE);
        NIMClient.getService(MsgService.class).downloadAttachment(message, true).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                Toast.makeText(context, "缩略图下载成功", Toast.LENGTH_SHORT).show();
                String path = ((ImageAttachment) message.getAttachment()).getThumbPath();
                if (!TextUtils.isEmpty(path)) {
                    viewHolder.leftLayout.setVisibility(View.VISIBLE);
                    viewHolder.leftFileLayout.setVisibility(View.GONE);
                    viewHolder.leftMsg.setVisibility(View.GONE);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    viewHolder.leftImg.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(context, "缩略图下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(context, "缩略图下载异常", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 显示收到的文件消息
     *
     * @param message
     * @param viewHolder
     */
    private void showReceiveFileMessage(final IMMessage message, final ViewHolder viewHolder) {
        viewHolder.rightLayout.setVisibility(View.GONE);
        viewHolder.leftLayout.setVisibility(View.VISIBLE);
        viewHolder.leftMsg.setVisibility(View.GONE);
        viewHolder.leftImg.setVisibility(View.GONE);
        NIMClient.getService(MsgService.class).downloadAttachment(message, true).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                Toast.makeText(context, "文件下载成功", Toast.LENGTH_SHORT).show();
                FileAttachment attachment = (FileAttachment) message.getAttachment();
                String fileName = attachment.getFileName();
                String extension = attachment.getExtension();
                viewHolder.leftFileName.setText(fileName + "." + extension);
                if (extension != null) {
                    if ("txt".equals(extension)) {
                        viewHolder.leftFileImage.setImageResource(R.drawable.icon_delete);
                    } else if ("doc".equals(extension) || "docx".equals(extension)) {
                        viewHolder.leftFileImage.setImageResource(R.drawable.icon_addition);
                    }
                }
                long size = attachment.getSize();
                viewHolder.leftFileSize.setText(getSize(size));
                boolean fileHasDownloaded = isOriginFileHasDownloaded(message);
                if (fileHasDownloaded) {
                    viewHolder.leftFileStatus.setText("已下载");
                } else {
                    viewHolder.leftFileStatus.setText("未下载");
                }
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(context, "文件下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(context, "文件下载异常", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 下载之前判断一下是否已经下载。若重复下载，会报错误码414。（以SnapChatAttachment为例）
    private boolean isOriginFileHasDownloaded(final IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((FileAttachment) message.getAttachment()).getPath())) {
            return true;
        }
        return false;
    }
    //=================================发送=======================================

    /**
     * 显示发送的文本消息
     *
     * @param message
     * @param viewHolder
     */
    private void showSendTextMessage(IMMessage message, ViewHolder viewHolder) {
        viewHolder.leftLayout.setVisibility(View.GONE);
        viewHolder.rightLayout.setVisibility(View.VISIBLE);
        viewHolder.rightImg.setVisibility(View.GONE);
        viewHolder.rightFileLayout.setVisibility(View.GONE);
        viewHolder.rightMsg.setText(message.getContent());
    }

    /**
     * 显示发送的图片消息
     *
     * @param message
     * @param viewHolder
     */
    private void showSendImageMessage(IMMessage message, ViewHolder viewHolder) {
        viewHolder.leftLayout.setVisibility(View.GONE);
        viewHolder.rightLayout.setVisibility(View.VISIBLE);
        viewHolder.rightFileLayout.setVisibility(View.GONE);
        viewHolder.rightMsg.setVisibility(View.GONE);
        String path = ((ImageAttachment) message.getAttachment()).getPath();
        if (path == null) {
            return;
        }
        Bitmap bitmap = getImageThumbnail(path, 240, 350);
        viewHolder.rightImg.setImageBitmap(bitmap);
    }

    /**
     * 显示发送的文件消息
     *
     * @param message
     * @param viewHolder
     */
    private void showSendFileMessage(IMMessage message, ViewHolder viewHolder) {
        viewHolder.leftLayout.setVisibility(View.GONE);
        viewHolder.rightLayout.setVisibility(View.VISIBLE);
        viewHolder.rightMsg.setVisibility(View.GONE);
        viewHolder.rightImg.setVisibility(View.GONE);
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        String fileName = attachment.getFileName();
        String extension = attachment.getExtension();
        viewHolder.rightFileName.setText(fileName);
        if (extension != null) {
            if ("txt".equals(extension)) {
                viewHolder.rightFileImage.setImageResource(R.drawable.icon_delete);
            } else if ("doc".equals(extension) || "docx".equals(extension)) {
                viewHolder.rightFileImage.setImageResource(R.drawable.icon_addition);
            }
        }

        long size = attachment.getSize();
        viewHolder.rightFileSize.setText(getSize(size));
        viewHolder.rightFileStatus.setText("已发送");

    }

    /**
     * 得到附件的大小
     *
     * @param size
     * @return
     */
    private String getSize(long size) {
        StringBuilder sb = new StringBuilder();
        if (size / 1024 != 0) {
            size /= 1024;
            if (size / 1024 != 0) {
                size /= 1024;
                sb.append(size).append("MB");
            } else {
                sb.append(size).append("KB");
            }
        } else {
            sb.append(size).append("B");
        }
        return sb.toString();
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


}
