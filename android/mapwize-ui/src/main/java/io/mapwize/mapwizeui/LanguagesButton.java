package io.mapwize.mapwizeui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.mapwize.mapwizeui.R;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.map.MapwizeMap;

/**
 * Language button allows user to change the displayed language
 * This button will be shown if we are in a venue which has more than one available languages.
 */
public class LanguagesButton extends AppCompatImageButton {

    private AlertDialog alertDialog = null;
    private List<String> languages = new ArrayList<>();
    private OnLanguageClickListener listener;

    public LanguagesButton(Context context) {
        super(context);
        initialize();
    }

    public LanguagesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public LanguagesButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setVisibility(View.GONE);
        this.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater == null) {
                return;
            }
            final View dialogView = inflater.inflate(R.layout.mapwize_languages_alert, null);
            RecyclerView languagesList = dialogView.findViewById(R.id.mapwizeLanguagesList);
            LanguagesAdapter languagesAdapter = new LanguagesAdapter();
            languagesList.setAdapter(languagesAdapter);
            languagesAdapter.swapData(languages);
            languagesAdapter.setListener(item -> {
                listener.onLanguageClick(item.getLanguage());
                alertDialog.dismiss();
            });
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    public void showIfNeeded() {
        if (languages.size() > 1) {
            setVisibility(View.VISIBLE);
        }
        else {
            setVisibility(View.GONE);
        }
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setListener(OnLanguageClickListener listener) {
        this.listener = listener;
    }

    /**
     * Adapter to display the list of languages in a recycler view
     */
    class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageItemViewHolder> {

        private List<Locale> mLanguages = new ArrayList<>();
        private OnItemClickListener mListener;

        void swapData(List<String> languages) {
            List<Locale> locales = new ArrayList<>();
            for (String l : languages) {
                locales.add(new Locale(l));
            }
            mLanguages = locales;
            notifyDataSetChanged();
        }

        void setListener(OnItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public LanguageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mapwize_text_item, parent, false);

            return new LanguageItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LanguageItemViewHolder holder, int position) {
            holder.itemView.setClickable(true);
            Locale language = mLanguages.get(position);
            holder.textView.setText(language.getDisplayLanguage().substring(0,1).toUpperCase() + language.getDisplayLanguage().substring(1));
        }

        @Override
        public int getItemCount() {
            return mLanguages != null ? mLanguages.size() : 0;
        }

        /**
         * View holder to display each language
         */
        class LanguageItemViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            LanguageItemViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_view);
                itemView.setOnClickListener(view -> {
                    int adapterPosition = getAdapterPosition();
                    if (mListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(mLanguages.get(adapterPosition));
                    }
                });
            }
        }
    }

    public interface OnLanguageClickListener {
        void onLanguageClick(String language);
    }

    public interface OnItemClickListener {
        void onItemClick(Locale item);
    }

}