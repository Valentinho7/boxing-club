package fr.eql.ai115.boxing.club;

import fr.eql.ai115.boxing.club.entity.Member;
import fr.eql.ai115.boxing.club.entity.dto.AddMemberDto;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import fr.eql.ai115.boxing.club.service.impl.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void testUpdateMember() {
        // Define test data
        Long memberId = 1L;
        AddMemberDto updateMemberDto = new AddMemberDto();
        updateMemberDto.setFirstname("John");
        updateMemberDto.setLastname("Doe");
        updateMemberDto.setBirthdate(LocalDate.parse("1990-01-01"));
        updateMemberDto.setEmail("john.doe@example.com");
        updateMemberDto.setPhoneNumber("1234567890");
        updateMemberDto.setAddress("123 Main St");

        Member member = new Member();
        member.setId(memberId);

        // Mock behavior
        when(memberService.findMemberById(memberId)).thenReturn(Optional.of(member));

        // Call method
        applicationService.updateMember(updateMemberDto, memberId);

        // Verify updates
        verify(memberService).save(argThat(updatedMember ->
                updatedMember.getId().equals(memberId) &&
                        updatedMember.getFirstname().equals("John") &&
                        updatedMember.getLastname().equals("Doe") &&
                        updatedMember.getBirthdate().equals(LocalDate.parse("1990-01-01")) &&
                        updatedMember.getEmail().equals("john.doe@example.com") &&
                        updatedMember.getPhoneNumber().equals("1234567890") &&
                        updatedMember.getAddress().equals("123 Main St")
        ));
    }
}