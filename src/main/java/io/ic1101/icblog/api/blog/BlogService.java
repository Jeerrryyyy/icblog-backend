package io.ic1101.icblog.api.blog;

import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {
}
