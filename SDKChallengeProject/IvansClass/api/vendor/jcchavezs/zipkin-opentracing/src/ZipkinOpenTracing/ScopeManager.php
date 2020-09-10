<?php

namespace ZipkinOpenTracing;

use OpenTracing\ScopeManager as OTScopeManager;
use OpenTracing\Span as OTSpan;

final class ScopeManager implements OTScopeManager
{
    /**
     * @var Scope
     */
    private $active;

    /**
     * {@inheritdoc}
     */
    public function activate(OTSpan $span, $finishSpanOnClose = true)
    {
        $this->active = new Scope($this, $span, $finishSpanOnClose);

        return $this->active;
    }

    /**
     * {@inheritdoc}
     */
    public function getActive()
    {
        return $this->active;
    }

    public function setActive(Scope $scope = null)
    {
        $this->active = $scope;
    }
}
