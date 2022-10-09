package com.swygbro.housemate.heart.service;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.heart.messages.CreateHeartLetter;
import com.swygbro.housemate.heart.messages.InputFirstHeartLetter;
import com.swygbro.housemate.heart.messages.InputSecondHeartLetter;
import com.swygbro.housemate.heart.messages.ViewMessage;
import com.swygbro.housemate.heart.repository.heart.HeartRepository;
import com.swygbro.housemate.heart.repository.letter.LetterRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Service
@RequiredArgsConstructor
public class LetterProcess {

    private final UUIDUtil uuidUtil;
    private final CurrentMemberUtil currentMemberUtil;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final HeartRepository heartRepository;
    private final ModelMapper modelMapper;

    public CreateHeartLetter writeFirst(InputFirstHeartLetter inputFirstHeartLetter) { // A가 편지를 쓴다.
        Member to = currentMemberUtil.getCurrentMemberANDGroupObject();
        Member from = memberRepository.findByMemberId(inputFirstHeartLetter.getFrom())
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));

        Heart heart = inputFirstHeartLetter.creatHeartEntity(uuidUtil.create(), to);
        Letter letter = inputFirstHeartLetter.createLetterEntity(uuidUtil.create(), from, heart, to.getZipHapGroup());

        Heart heartSave = heartRepository.save(heart);
        letterRepository.save(letter);
        return CreateHeartLetter.of(heartSave.getHeartId());
    }

    public List<ViewMessage> notReadMessageView(String userId) {  //B userId를 기준으로 가 읽지 않은 편지 조회하기
        Member to = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        List<Letter> letterList = letterRepository.findByUserIdAndNotRead(to);

        return createViewMessages(letterList);
    }

    @Transactional
    public CreateHeartLetter writeSecond(String heartId, InputSecondHeartLetter inputSecondHeartLetter) { // B가 편지를 쓴다.
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        Heart findBy = heartRepository.findByHeartId(heartId)
                .orElseThrow(null);
        Letter letter = letterRepository.findByHeart(findBy)
                .orElseThrow(null);
        // 편지쓰기
        Heart heart = letter.getHeart();
        Letter writeLetter = inputSecondHeartLetter.createLetterEntity(heartId, heart.getTo(), heart, currentMemberANDGroupObject.getZipHapGroup());

        letterRepository.save(writeLetter);

        // 읽음 처리
        heart.read();

        return CreateHeartLetter.of(heart.getHeartId());
    }

    public List<ViewMessage> viewMessage() { // Group 편지 전체 보기
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        List<Letter> letterList = letterRepository.findByGroupAndRead(currentMemberANDGroupObject.getZipHapGroup());

        return createViewMessages(letterList);
    }

    private List<ViewMessage> createViewMessages(List<Letter> letterList) {
        List<ViewMessage> viewMessageList = new ArrayList<>();
        for (Letter letterDomain : letterList) {
            MemberInfo toDto = modelMapper.map(letterDomain.getHeart().getTo(), MemberInfo.class);
            MemberInfo fromDto = modelMapper.map(letterDomain.getFrom(), MemberInfo.class);

            viewMessageList.add(ViewMessage.builder()
                    .letterId(letterDomain.getLetterId())
                    .heartId(letterDomain.getHeart().getHeartId())
                    .title(letterDomain.getTitle())
                    .content(letterDomain.getContent())
                    .kind(letterDomain.getKind())
                    .to(toDto)
                    .from(fromDto)
                    .build());
        }
        return viewMessageList;
    }
}
