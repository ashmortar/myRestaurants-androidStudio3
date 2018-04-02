package com.example.guest.myrestaurants.util;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;

    //  This constructor takes an ItemTouchHelperAdapter parameter. When implemented in
    //  FirebaseRestaurantListAdapter, the ItemTouchHelperAdapter instance will pass the gesture event back to the
    //  Firebase adapter where we will define what occurs when an item is moved or dismissed.

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    //The method below informs the ItemTouchHelperAdapter that drag gestures area enabled.  We could also disable drag gestures by returning false

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //The method below informs the ItemTouchHelperAdapter that swipe gestures are enabled.  WE could also disable them by returning false

    @Override
    public boolean  isItemViewSwipeEnabled() {
        return true;
    }

    //getMovementFlags informs the ItemTouhHelper which movement directions are supported. I.E. when user drags a list item the press "down" to begin the drag and lift their finger to end the drag

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    //the method below notifies teh adapter that an item has moved. This triggers the onItemMove override in our firbase adapter which will eventully handle updating hte restaurants arraylist to refelct the item's new position.

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //the method below notifies teh adapter that an item was dismissed. this triggers the onItemDismiss override in our firebase adapter which will eventually handle deleting htis item from the user's saved list

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }


    //this method triggers the callback in ItemTouchHelperViewHolder which is then sent to our RestaurantListViewHolder where we will later add animations

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // this conditional ensure we only change appearance of active items:
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof  ItemTouchHelperViewHolder) {

                // this tells the viewHolder that an item is being moved or dragged

                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //this triggers the callback in the ITemTouchHelperViewHolder which will be sent to the RestaurantListViewHolder.  Then, in the clearView override in RestaurantListViewHolder, we will remove the animations attached to 'selected' items, since this item will no longer be actively selected

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {

            //tells hte view holder to return the item back to its normal appearance:

            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
