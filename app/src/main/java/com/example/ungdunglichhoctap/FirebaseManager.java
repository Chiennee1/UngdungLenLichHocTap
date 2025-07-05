package com.example.ungdunglichhoctap;

import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import DoiTuong.User;
import DoiTuong.Lessons;
import DoiTuong.Subject;
import DoiTuong.Task;
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public static final String DATA_TYPE_TASKS = "tasks";
    public static final String DATA_TYPE_LESSONS = "lessons";
    public static final String DATA_TYPE_SUBJECTS = "subjects";
    public static final String DATA_TYPE_PROMODOTIME = "promodotime";
    private Button btnDangKy, btnGoogleSignUp;
    private final DatabaseReference databaseReference;
    private final FirebaseAuth mAuth;
    private final String userId;

    public interface DatabaseCallback<T> {
        void onSucces(T dataResult);

        void onError(String error);
    }

    public FirebaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return userId != null;
    }

    public String getUserId() {
        return userId;
    }

    // generic method to create data in Firebase
    public <T> void createUserData(String dataType, T dataResult, DatabaseCallback<String> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        String path = "users/" + userId + "/" + dataType;
        String key = databaseReference.child(path).push().getKey();
        if (key == null) {
            callback.onError("Không thể tạo khóa duy nhất cho dữ liệu");
            return;
        }
        databaseReference.child(path).child(key)
                .setValue(dataResult)
                .addOnSuccessListener(aVoid -> callback.onSucces(key))
                .addOnFailureListener(e -> callback.onError("Lỗi khi tạo dữ liệu: " + e.getMessage()));
    }
    // phuong thức lấy thông tin người dùng hiện tại
    public void getUserInfo(DatabaseCallback<User> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
       String path = "users/" + userId;
        databaseReference.child(path)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()) {
                            callback.onError("Không tìm thấy thông tin người dùng");
                            return;
                        }
                        User user = snapshot.getValue(User.class);
                        if(user != null){
                            callback.onSucces(user);
                        }
                        else{
                            callback.onError("Không tìm thấy thông tin người dùng");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Lỗi khi đọc dữ liệu: " + error.getMessage());
                    }
                });
    }
    // Generic method for Read data from Firebase
    public <T> void readUserData(String path, Class<T> typeOClass, DatabaseCallback<T> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        databaseReference.child(path)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<T> dataList = new ArrayList<>();
                        for (DataSnapshot dataSanphot : snapshot.getChildren()) {
                            T data = dataSanphot.getValue(typeOClass);
                            if (data != null) {
                                dataList.add(data);
                            }
                        }
                        callback.onSucces((T) dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Lỗi khi đọc dữ liệu: " + error.getMessage());
                    }
                });
    }

    // Generic method read one Object from Firebase
    public <T> void getData(String path, String itemID, Class<T> typeOClass, DatabaseCallback<T> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        databaseReference.child(path).child(itemID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            callback.onError("Không tìm thấy dữ liệu với ID: " + itemID);
                            return;
                        }
                        T data = snapshot.getValue(typeOClass);
                        if (data != null) {
                            callback.onSucces(data);
                        } else {
                            callback.onError("Không tìm thấy dữ liệu");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Lỗi khi đọc dữ liệu: " + error.getMessage());
                    }
                });
    }
    // Generic method to update data in Firebase
    public <T> void updateUserData(String dataType, String itemID, T dataResult, DatabaseCallback<T> callback){
        if (!isUserLoggedIn()){
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        String path = "users/" + userId + "/" + dataType;
        databaseReference.child(path).child(itemID)
                .setValue(dataResult)
                .addOnSuccessListener(aVoid -> callback.onSucces(dataResult))
                .addOnFailureListener(e -> callback.onError("Lỗi khi cập nhật dữ liệu: " + e.getMessage()));

    }

    // Generic method to delete data in Firebase
    public <T> void deleteData(String dataType, String itemID, DatabaseCallback<Boolean> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        String path = "users/" + userId + "/" + dataType;
        databaseReference.child(path).child(itemID)
                .removeValue()
                .addOnSuccessListener(aVoid -> callback.onSucces(true))
                .addOnFailureListener(e -> callback.onError("Lỗi khi xóa dữ liệu: " + e.getMessage()));
    }
    // phương thức đặc biệt cho query dữ liệu của người dùng hiện tại
    public <T> void queryUserByField(String dataType, String fieldName, Objects value, Class<T> typeOClass, DatabaseCallback<T> callback){
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        String path = "users/" + userId + "/" + dataType;
        databaseReference.child(path)
                .orderByChild(fieldName)
                .equalTo(String.valueOf(value))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<T> dataList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            T data = dataSnapshot.getValue(typeOClass);
                            if (data != null) {
                                dataList.add(data);
                            }
                        }
                        callback.onSucces((T) dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Lỗi khi truy vấn dữ liệu: " + error.getMessage());
                    }
                });
    }
    // phương thức đặc biệt cho query dữ liệu của người dùng hiện tại
    public <T> void queryUserDataByTimeRange(String dataType, String timeField, long startTime, long endTime, Class<T> typeClass, DatabaseCallback<List<T>> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
         String path = "users/" +userId + "/" + dataType;
        databaseReference.child(path)
                .orderByChild(timeField)
                .startAt(startTime)
                .endAt(endTime)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<T> itemList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            T item = snapshot.getValue(typeClass);
                            if (item != null) {
                                itemList.add(item);
                            }
                        }
                        callback.onSucces(itemList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError("Lỗi truy vấn dữ liệu: " + databaseError.getMessage());
                    }
                });
    }
    // Phuowng thức lay dữ liệu con của user(Task, event, note)
    public <T> void getUserData(String dataType, Class<T> typeOClass, DatabaseCallback<List<T>> callback) {
        if (!isUserLoggedIn()) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        String path = "users/" + userId + "/" + dataType;
        databaseReference.child(path)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<T> dataList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            T data = dataSnapshot.getValue(typeOClass);
                            if (data != null) {
                                dataList.add(data);
                            }
                        }
                        callback.onSucces(dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError("Lỗi khi đọc dữ liệu: " + error.getMessage());
                    }
                });
    }
    public void createTask(Task task, DatabaseCallback<String> callback) {
        createUserData(DATA_TYPE_TASKS, task, callback);
    }

    public void createLesson(Lessons lesson, DatabaseCallback<String> callback) {
        createUserData(DATA_TYPE_LESSONS, lesson, callback);
    }

    public void createSubject(Subject subject, DatabaseCallback<String> callback) {
        createUserData(DATA_TYPE_SUBJECTS, subject, callback);
    }

    // Tương tự cho read, update, delete
    public void getTasks(DatabaseCallback<List<Task>> callback) {
        getUserData(DATA_TYPE_TASKS, Task.class, callback);
    }

    public void getLessons(DatabaseCallback<List<Lessons>> callback) {
        getUserData(DATA_TYPE_LESSONS, Lessons.class, callback);
    }
}
