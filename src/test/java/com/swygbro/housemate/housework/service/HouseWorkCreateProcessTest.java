package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.housework.service.cycle.CycleFactory;
import com.swygbro.housemate.login.service.ManagedFactory;
import com.swygbro.housemate.util.condition.CycleCondition;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseWorkCreateProcessTest {

    HouseWorkCreateProcess sut;

    @Mock
    CreateHouseWork createHouseWork;

    @Mock
    HouseWorkRepository houseWorkRepository;
    @Mock
    CycleRepository cycleRepository;

    @Mock
    UUIDUtil uuidUtil;

    @Mock
    CycleCondition cycleCondition;
    @Mock
    CycleFactory cycleFactory;
    @Mock
    ManagedFactory managedFactory;


//    @BeforeEach
//    void setUp() {
//        sut = new HouseWorkCreateProcess(managedFactory, houseWorkRepository, uuidUtil, cycleCondition, cycleFactory);
//    }

//    @Test
//    void when_create_house_work_cycle_true_success() {
//        given(createHouseWork.getIsCycle()).willReturn(true);
//
//
//
//        sut.execute(createHouseWork);
//
//        verify(cycleFactory).create(houseWorkId);
//        verify(managerFatory).create(memberId);
//        verify(groupFatory).create(groupId);
//        verify(successHandler).onHandle(hoseWorkRes);
//        verifyNoMoreInteractions(failHandler);
//    }
//
//    @Test
//    void when_create_house_work_cycle_true_fail() {
//        given(createHouseWork.getIsCycle()).willReturn(true);
//
//        sut.execute(createHouseWork);
//
//        verify(cycleFatory).create();
//        verify(managerFatory).create();
//        verify(groupFatory).create();
//        verify(failHandler).onHandle(hoseWorkRes);
//        verifyNoMoreInteractions(successHandler);
//    }
}