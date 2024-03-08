//package com.example.scansaga;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//
//@RunWith(AndroidJUnit4.class)
//public class ImageAdapterTest {
//
//    private Context context;
//    private ArrayList<String> imageList;
//    private ImageAdapter imageAdapter;
//
//    @Before
//    public void setUp() {
//        context = ApplicationProvider.getApplicationContext();
//        imageList = new ArrayList<>();
//        imageList.add("url1");
//        imageList.add("url2");
//        imageAdapter = new ImageAdapter(imageList, context);
//    }
//
//    @Test
//    public void testGetItemCount() {
//        assertEquals(imageList.size(), imageAdapter.getItemCount());
//    }
//
//    @Test
//    public void testViewHolder() {
//        ViewGroup parent = Mockito.mock(ViewGroup.class);
//        View itemView = LayoutInflater.from(context).inflate(R.layout.all_images, parent, false);
//        ImageAdapter.ViewHolder viewHolder = imageAdapter.new ViewHolder(itemView);
//
//        assertEquals(itemView.findViewById(R.id.item), viewHolder.imageView);
//        assertEquals(itemView.findViewById(R.id.delete_button), viewHolder.deleteButton);
//    }
//
////    @Test
////    public void testOnBindViewHolder() {
////        ImageAdapter.ViewHolder viewHolder = Mockito.mock(ImageAdapter.ViewHolder.class);
////        Mockito.when(viewHolder.getAdapterPosition()).thenReturn(0);
////
////        imageAdapter.onBindViewHolder(viewHolder, 0);
////
////        verify(viewHolder).getAdapterPosition();
////        verify(viewHolder.imageView).getContext();
////        verify(viewHolder.imageView).setImageResource(R.drawable.placeholder_image); // Replace with your placeholder image resource
////    }
//
//    @Test
//    public void testOnDeleteClickListener() {
//        ImageAdapter.OnDeleteClickListener listener = Mockito.mock(ImageAdapter.OnDeleteClickListener.class);
//        imageAdapter.setOnDeleteClickListener(listener);
//
//        imageAdapter.onBindViewHolder(Mockito.mock(ImageAdapter.ViewHolder.class), 0);
//        imageAdapter.onBindViewHolder(Mockito.mock(ImageAdapter.ViewHolder.class), 1);
//
//        imageAdapter.setOnDeleteClickListener(null); // Clear listener
//
//        // Test delete button click
//        imageAdapter.onBindViewHolder(Mockito.mock(ImageAdapter.ViewHolder.class), 0);
//        imageAdapter.onBindViewHolder(Mockito.mock(ImageAdapter.ViewHolder.class), 1);
//
//        verify(listener).onDeleteClick(0);
//        verify(listener).onDeleteClick(1);
//    }
//}
//
