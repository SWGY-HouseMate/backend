package com.swygbro.housemate.heart.service;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.heart.messages.*;
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

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.*;

@Service
@RequiredArgsConstructor
public class WireLetterProcess {
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final HeartRepository heartRepository;
    private final UUIDUtil uuidUtil;
    private final CurrentMemberUtil currentMemberUtil;
    private final ModelMapper modelMapper;

    // TODO :: 일단은 무한으로 작성 -> 바꾸기 (생성 시점을 Client 에게 받기)
    public CreateHeartLetter writeFirst(InputFirstHeartLetter inputFirstHeartLetter) { // A가 편지를 쓴다.
        Member from = currentMemberUtil.getCurrentMemberANDGroupObject();
        Member to = memberRepository.findByMemberId(inputFirstHeartLetter.getFrom())
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));

        Heart heart = inputFirstHeartLetter.creatHeartEntity(uuidUtil.create());
        Letter letter = inputFirstHeartLetter.createLetterEntity(uuidUtil.create(),
                from.getMemberId(),
                to.getMemberId(),
                heart,
                to.getZipHapGroup());

        Heart heartSave = heartRepository.save(heart);
        letterRepository.save(letter);
        return CreateHeartLetter.of(heartSave.getHeartId());
    }

    public List<PrivateViewMessage> notReadMessageView(String userId) {  //B userId를 기준으로 가 읽지 않은 편지 조회하기
        Member from = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        List<Letter> letterList = letterRepository.findByUserIdAndNotRead(from.getMemberId());

        return createPrivateViewMessages(letterList);
    }

    @Transactional
    public CreateHeartLetter writeSecond(String heartId, InputSecondHeartLetter inputSecondHeartLetter) { // B가 편지를 쓴다.
        Heart findBy = heartRepository.findByHeartId(heartId)
                .orElseThrow(() -> new DataNotFoundException(편지를_찾을_수_없습니다));
        Letter letter = letterRepository.findByHeart(findBy)
                .orElseThrow(() -> new DataNotFoundException(편지의_내용을_찾을_수_없습니다));
        // 편지쓰기
        Heart heart = letter.getHeart();
        Letter writeLetter = inputSecondHeartLetter.createLetterEntity(
                uuidUtil.create(),
                letter.getLetterTo(),
                letter.getLetterFrom(),
                heart,
                letter.getGroup()
        );

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
            MemberInfo toDto = modelMapper.map(
                    memberRepository.findByMemberId(letterDomain.getLetterTo()).orElseThrow(null),
                    MemberInfo.class
            );
            MemberInfo fromDto = modelMapper.map(
                    memberRepository.findByMemberId(letterDomain.getLetterFrom()).orElseThrow(null),
                    MemberInfo.class
            );

            viewMessageList.add(ViewMessage.builder()
                    .letterId(letterDomain.getLetterId())
                    .heartId(letterDomain.getHeart().getHeartId())
                    .content(letterDomain.getContent())
                    .kind(letterDomain.getHeartType())
                    .createAt(letterDomain.createAt())
                    .to(toDto)
                    .from(fromDto)
                    .build());
        }
        return viewMessageList;
    }

    private List<PrivateViewMessage> createPrivateViewMessages(List<Letter> letterList) {
        List<PrivateViewMessage> viewMessageList = new ArrayList<>();
        for (Letter letterDomain : letterList) {
            MemberInfo toDto = modelMapper.map(
                    memberRepository.findByMemberId(letterDomain.getLetterTo()).orElseThrow(null),
                    MemberInfo.class
            );
            MemberInfo fromDto = modelMapper.map(
                    memberRepository.findByMemberId(letterDomain.getLetterFrom()).orElseThrow(null),
                    MemberInfo.class
            );

            viewMessageList.add(PrivateViewMessage.builder()
                    .letterId(letterDomain.getLetterId())
                    .heartId(letterDomain.getHeart().getHeartId())
                    .heartType(letterDomain.getHeartType())
                    .createAt(letterDomain.createAt())
                    .to(toDto)
                    .from(fromDto)
                    .build());
        }
        return viewMessageList;
    }
}
