<?php

namespace ZipkinOpenTracing;

use OpenTracing\Scope as OTScope;
use OpenTracing\Span as OTSpan;

final class Scope implements OTScope
{
    /**
     * @var ScopeManager
     */
    private $scopeManger;

    /**
     * @var OTSpan
     */
    private $wrapped;

    /**
     * @var bool
     */
    private $finishSpanOnClose;

    public function __construct(ScopeManager $scopeManager, OTSpan $wrapped, $finishSpanOnClose)
    {
        $this->scopeManager = $scopeManager;
        $this->wrapped = $wrapped;
        $this->finishSpanOnClose = $finishSpanOnClose;
        $this->toRestore = $scopeManager->getActive();
    }

    /**
     * {@inheritdoc}
     */
    public function getSpan()
    {
        return $this->wrapped;
    }

    /**
     * {@inheritdoc}
     */
    public function close()
    {
        if ($this->scopeManager->getActive() !== $this) {
            // This shouldn't happen if users call methods in expected order
            return;
        }

        if ($this->finishSpanOnClose) {
            $this->wrapped->finish();
        }

        $this->scopeManager->setActive($this->toRestore);
    }
}
