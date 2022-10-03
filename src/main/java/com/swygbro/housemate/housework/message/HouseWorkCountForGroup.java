package com.swygbro.housemate.housework.message;


import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
@Value(staticConstructor = "of")
public class HouseWorkCountForGroup {

    List<HouseWorkCountInfo> houseWorkCountInfoList;

}
