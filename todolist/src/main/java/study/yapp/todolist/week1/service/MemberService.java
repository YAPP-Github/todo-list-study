package study.yapp.todolist.week1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.yapp.todolist.common.ResponseCode;
import study.yapp.todolist.exception.DuplicateUserException;
import study.yapp.todolist.exception.InvalidUserException;
import study.yapp.todolist.week1.repository.MemberRepository;
import study.yapp.todolist.week1.dao.Member;
import study.yapp.todolist.dto.MemberDto;

//@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param request
     * @return
     */
    public MemberDto.ResponseMemberDto signUp(MemberDto.RequestSignUpDto request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 유저입니다.", ResponseCode.DUPLICATE_USER);
        }

        Member member = Member.builder()
                .member_id(memberRepository.MEMBER_INDEX.getAndIncrement())
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();

        memberRepository.saveMember(member);

        MemberDto.ResponseMemberDto result = MemberDto.ResponseMemberDto.builder()
                                                                        .memberId(member.getMember_id())
                                                                        .build();

        return result;
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    public MemberDto.ResponseMemberDto signIn(MemberDto.RequestSignInDto request) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).get();

        if (member == null) {
            throw new InvalidUserException("존재하지 않는 유저입니다.", ResponseCode.INVALID_USER);
        }

        MemberDto.ResponseMemberDto result = MemberDto.ResponseMemberDto.builder()
                .memberId(member.getMember_id())
                .build();

        return result;
    }
}