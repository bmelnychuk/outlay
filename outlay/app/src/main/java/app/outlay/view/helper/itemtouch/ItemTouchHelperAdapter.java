package app.outlay.view.helper.itemtouch;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}.
 *
 * @author Paul Burke (ipaulpro)
 */
public interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
     * <br/>
     * Implementations should call {@link RecyclerView.Adapter#notifyItemMoved(int, int)} after
     * adjusting the underlying data to reflect this move.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     * @return True if the item was moved to the new adapter position.
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.<br/>
     * <br/>
     * Implementations should call {@link RecyclerView.Adapter#notifyItemRemoved(int)} after
     * adjusting the underlying data to reflect this removal.
     *
     * @param position The position of the item dismissed.
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemDismiss(int position);

    /**
     * Called by the ItemTouchHelper when the user interaction with an element is over and it
     * also completed its animation.
     * <p>
     * This is a good place to clear all changes on the View that was done in
     * {@link SimpleItemTouchHelperCallback#onSelectedChanged(RecyclerView.ViewHolder, int)},
     * {@link SimpleItemTouchHelperCallback#onChildDraw(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int,
     * boolean)} or
     * {@link SimpleItemTouchHelperCallback#onChildDrawOver(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)}.
     *
     * @param recyclerView The RecyclerView which is controlled by the ItemTouchHelper.
     * @param viewHolder   The View that was interacted by the user.
     */
    void onInteractionComplete(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
}
