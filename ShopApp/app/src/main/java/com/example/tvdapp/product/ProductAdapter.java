package com.example.tvdapp.product;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private List<ProductViewEntity> productViewEntities;
    private List<ProductViewEntity> productViewEntitiesFiltered;
    private Context context;
    private ProductViewHolderEvent event;

    public void setEvent(ProductViewHolderEvent event) {
        this.event = event;
    }

    public List<ProductViewEntity> getProductViewEntities() {
        return productViewEntities;
    }

    public ProductAdapter(List<ProductViewEntity> productViewEntities, Context context) {
        this.productViewEntities = productViewEntities;
        this.productViewEntitiesFiltered = productViewEntities;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_product, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productView, context, event);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductViewEntity entity = productViewEntitiesFiltered.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return productViewEntitiesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence.toString().isEmpty()) {
                    productViewEntitiesFiltered = productViewEntities;
                } else {
                    productViewEntitiesFiltered = productViewEntities.stream()
                            .filter(product -> product.name.contains(charSequence))
                            .collect(Collectors.toList());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productViewEntitiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productViewEntitiesFiltered = (List<ProductViewEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addProduct(ProductViewEntity entity) {
        productViewEntities.add(entity);
        notifyItemInserted(productViewEntitiesFiltered.size() - 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeProduct(ProductViewEntity entity) {
        ProductViewEntity oldProduct = productViewEntities.stream()
                .filter(product -> product.id.compareTo(entity.id) == 0)
                .findAny()
                .orElse(null);
        if (oldProduct != null) {
            oldProduct.name = entity.name;
            oldProduct.imageLink = entity.imageLink;
            oldProduct.amount = entity.amount;
            oldProduct.price = entity.price;
            oldProduct.selectionStatus = entity.selectionStatus;
        }

        reloadProductItem(entity.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeProduct(String id) {
        ProductViewEntity entity = productViewEntities.stream()
                .filter(product -> product.id.compareTo(id) == 0)
                .findAny()
                .orElse(null);
        if (entity != null) {
            int index = getIndexById(id);
            if (index != -1) {
                notifyItemRemoved(index);
            }

            productViewEntities.remove(entity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchProductImage(String id, String imageLink) {
        ProductViewEntity entity = productViewEntities.stream()
                .filter(product -> product.id.compareTo(id) == 0)
                .findAny()
                .orElse(null);
        if (entity != null) {
            entity.imageLink = imageLink;
            reloadProductItem(id);
        }
    }

    public void turnOnDeleteMode() {
        for (ProductViewEntity entity : productViewEntities) {
            entity.selectionStatus = SelectionProductStatus.unchecked;
        }
    }

    public void turnOffDeleteMode() {
        for (ProductViewEntity entity : productViewEntities) {
            entity.selectionStatus = SelectionProductStatus.hide;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectCheckbox(String id, boolean isChecked) {
        ProductViewEntity entity = productViewEntities.stream()
                .filter(product -> product.id.compareTo(id) == 0)
                .findAny()
                .orElse(null);
        if (entity != null) {
            if (isChecked) {
                entity.selectionStatus = SelectionProductStatus.checked;
            } else {
                entity.selectionStatus = SelectionProductStatus.unchecked;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getSelectedProductCount() {
        return productViewEntities.stream()
                .reduce(0, (total, product) -> total + (product.selectionStatus == SelectionProductStatus.checked ? 1 : 0), Integer::sum);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ProductViewEntity> getSelectedProducts() {
        return productViewEntities.stream()
                .filter(product -> product.selectionStatus == SelectionProductStatus.checked)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reloadProductItem(String id) {
        int index = getIndexById(id);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getIndexById(String id) {
        int index = IntStream.range(0, productViewEntitiesFiltered.size())
                .filter(i -> productViewEntitiesFiltered.get(i).id.compareTo(id) == 0)
                .findFirst()
                .orElse(-1);
        return index;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView amountTextView;
        private TextView priceTextView;
        private CheckBox checkBox;
        private Context context;
        private ProductViewHolderEvent event;
        public ProductViewEntity entity;

        public ProductViewHolder(@NonNull View itemView, Context context, ProductViewHolderEvent event) {
            super(itemView);
            this.context = context;
            this.event = event;

            imageView = itemView.findViewById(R.id.product_image_view);
            nameTextView = itemView.findViewById(R.id.product_name_text);
            amountTextView = itemView.findViewById(R.id.product_amount_text);
            priceTextView = itemView.findViewById(R.id.product_price_text);
            checkBox = itemView.findViewById(R.id.product_checkbox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        event.selectProductView(entity.id);
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (event != null) {
                        event.selectCheckbox(entity.id, b);
                    }
                }
            });
        }

        private String getAmountString(int amount) {
            String description = context.getString(R.string.product_amount_description);
            return String.format("%s: %d", description, amount);
        }

        public void bindData(ProductViewEntity entity) {
            this.entity = entity;
            nameTextView.setText(entity.name);
            amountTextView.setText(getAmountString(entity.amount));
            priceTextView.setText(entity.price);
            updateStatusCheckbox(entity.selectionStatus);
            Glide.with(context)
                    .load(entity.imageLink)
                    .centerCrop()
                    .into(imageView);
        }

        public void updateStatusCheckbox(SelectionProductStatus status) {
            switch (status) {
                case hide:
                    checkBox.setVisibility(View.INVISIBLE);
                    break;
                case checked:
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                    break;
                default:
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(false);
                    break;
            }
        }
    }

    interface ProductViewHolderEvent {
        void selectProductView(String productId);

        void selectCheckbox(String id, boolean isChecked);
    }
}
