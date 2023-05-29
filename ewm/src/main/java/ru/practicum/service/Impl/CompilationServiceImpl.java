package ru.practicum.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        List<Event> events = new ArrayList<>();
        if (newCompilation.getEvents() != null) {
            events = eventRepository.getByIdIn(newCompilation.getEvents());
        }
        Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(newCompilation, events));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updatedCompilation) {

        Compilation compilationForUpdate = getCompilationById(compId);
        List<Event> events = new ArrayList<>();
        if (updatedCompilation.getEvents() != null) {
            events = eventRepository.getByIdIn(updatedCompilation.getEvents());
        }
        Optional.ofNullable(updatedCompilation.getPinned()).ifPresent(compilationForUpdate::setPinned);
        Optional.ofNullable(updatedCompilation.getTitle()).ifPresent(compilationForUpdate::setTitle);
        Optional.ofNullable(events).ifPresent(compilationForUpdate::setEvents);
        Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(compilationForUpdate,
                events));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getComplicationsPublic(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Compilation> pageCompilation;
        if (pinned != null) {
            pageCompilation = compilationRepository.findAllByPinned(pinned, pageRequest);
        } else {
            pageCompilation = compilationRepository.findAll(pageRequest);
        }
        List<Compilation> requests = pageCompilation.getContent();
        return compilationMapper.toCompilationDto(requests);
    }

    @Override
    public CompilationDto getCompilationByIdPublic(Long compId) {
        return compilationMapper.toCompilationDto(getCompilationById(compId));
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation not found.")
        );
    }

    @Override
    public void deleteComplication(Long compId) {
        compilationRepository.deleteById(compId);
    }
}
