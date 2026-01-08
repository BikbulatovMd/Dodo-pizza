package sk.food.dodopizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.food.dodopizzeria.entity.Tag;
import sk.food.dodopizzeria.exception.ResourceNotFoundException;
import sk.food.dodopizzeria.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", id));
    }

    public Tag findBySlug(String slug) {
        return tagRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "slug", slug));
    }

    @Transactional
    public Tag createTag(String nameSk, String slug) {
        if (tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Tag so slugom '" + slug + "' už existuje");
        }

        Tag tag = new Tag();
        tag.setNameSk(nameSk);
        tag.setSlug(slug);
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Long id, String nameSk, String slug) {
        Tag tag = findById(id);

        if (!tag.getSlug().equals(slug) && tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Tag so slugom '" + slug + "' už existuje");
        }

        tag.setNameSk(nameSk);
        tag.setSlug(slug);
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = findById(id);
        if (!tag.getPizzas().isEmpty()) {
            throw new IllegalStateException("Nemožno vymazať tag, ktorý je priradený k pizzám");
        }
        tagRepository.delete(tag);
    }
}

