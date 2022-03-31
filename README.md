# ReminderApp

### 간단 설명
room을 이용해 데이터를 저장했으며, 리마인드를 추가, 수정, 삭제할 수 있습니다. 지정한 시간에 알람이 울리며, 8시 10분에 8시 10분 알람을 설정할 경우 다음날의 8시 10분에 알람이 울립니다! 

알람 페이지에서 Dismiss 버튼을 누르면 알람의 토글이 꺼지며, 메인 페이지에서 토글 버튼을 눌러도 알람이 해제됩니다.

벨소리를 선택하지 않을 시 기본 벨소리가 울리며 다른 벨소리로 선택 또한 가능합니다.

---
### 실행 영상
![실행 영상](https://user-images.githubusercontent.com/67602108/161024708-88771c3b-770d-4499-a386-f7fbbdfeaa8b.gif)

---
### 사용 기술 스택
1. Kotlin
2. AndroidX
3. MVVM
4. AAC ViewModel, Databinding, LiveData
5. Room
6. Jetpack navigation
7. Coroutine
8. Rxbinding
9. hilt
---
### 프로젝트 구조
```kotlin
─com
    └─delightroom
        └─reminder
            │  ReminderApplication.kt
            ├─base
            │      BaseFragment.kt
            ├─component
            │      AlarmReceiver.kt
            │      AlarmService.kt
            ├─repository
            │  │  ReminderData.kt
            │  │  ReminderRepository.kt
            │  ├─local
            │  │      LocalRemindDataSource.kt
            │  │      LocalRemindDataSourceImpl.kt
            │  └─room
            │          ReminderDao.kt
            │          ReminderDatabase.kt
            ├─ui
            │  │  AlarmActivity.kt
            │  │  MainActivity.kt
            │  ├─main
            │  │  │  ReminderMainFragment.kt
            │  │  │
            │  │  └─recyclerview
            │  │          ReminderRecyclerviewAdapter.kt
            │  │          ReminderViewHolder.kt
            │  └─setting
            │          ReminderEditingFragment.kt
            ├─util
            │      BindingAdapter.kt
            │      NonNullLiveData.kt
            │      ReminderModule.kt
            │      StringUtils.kt
            └─viewmodel
                    ReminderViewModel.kt                       
  ```
#### base
  - BaseFragment를 작성했습니다. 사용되는 Fragment 화면의 개수는 적지만, 확장 가능성을 염두해두고 작성했습니다. 메모리 해제나 기본 binding의 기능을 넣었습니다.
  
#### componet
  - BroadCast Receiver와 Service에 대해서 만든 AlarmReceiver와 AlarmService를 위치했습니다.
  Activity 에서 BroadCast Receiver로, BroadCast Receiver에서 Service로 데이터를 보내며,
  Service에서 Notification을 생성해 사용자에게 알람을 보냅니다.

#### repository
- 보통 local과 remote를 둘다 쓴다면 repository 패턴을 이용해 두개의 기능을 묶어 viewmodel 단에서는 add함수만 호출하면, remote로 가져와 local에 넣고, 그 리스트를 반환하는 등과 같이 기능을 캡슐화하고 추상화하여 받아올 수 있지만, 해당 프로젝트에서는 local만 쓰기 때문에 한층 더 나눈다는 개념으로 ReminderRepository를 만들었습니다.

- ReminderData또한 이곳에 위치했는데, 실제 사용되는 데이터와 Room에서 쓰이는 데이터를 Mapper를 이용해 데이터를 나눌까 생각도 해봤지만, 일단은 나누지 않고 그대로 사용했습니다.

- Room을 이용한 CRUD를 구현했으며, getAll함수를 이용해 모든 데이터를 리스트로 받을 때, ORDER BY time을 이용해 시간순으로 sort하여 가져오게끔 만들었습니다.
  
#### ui
- MainActivity에 RecyclerView를 보여주고 Add 할 수 있는 메인 페이지와, 상세 Edit Page를 프래그먼트로 만들었습니다. 해당 페이지에서 리마인더를 add하고 update할 수 있으며, 요소 리스트들을 보여줍니다.

- save버튼을 누르면 알람을 등록하며, 토글 기능을 통해 알람을 끄면, 알람을 cancel 해줍니다.
  
#### util
- 유틸에는 BindingAdapter, NonNullLiveData, Hilt Module, String Utils를 만들었으며, 자주쓰는 String 함수들이나 상수값들을 모아두었습니다.

#### viewmodel
- 뷰모델에서는 Livedata를 이용해 data binding observe를 사용할 수 있게끔 만들었으며, add, delete, update, getAll 함수를 만들어 리스트의 CRUD를 할 수 있게끔 구성했습니다. 

- add 함수에서는 long을 return으로 받아 add 성공시에 해당 아이템의 id를 리턴하게하여, 해당 id를 알람의 id로 설정하여 언제든 알람을 켜고, 끌 수 있도록 만들었습니다.
  
### 테스트 환경
- 사용 언어 : Kotlin
- minSdk - 24
- targetSdk - 31
- 테스트 기기 - Pixel 4 API 31
